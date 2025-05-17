package com.valchev.athletiq.service;

import com.valchev.athletiq.domain.dto.WorkoutHistoryDTO;
import com.valchev.athletiq.domain.dto.WorkoutStats;
import com.valchev.athletiq.domain.entity.ExerciseHistory;
import com.valchev.athletiq.domain.entity.ExerciseSetHistory;
import com.valchev.athletiq.domain.entity.WorkoutHistory;
import com.valchev.athletiq.domain.exception.ResourceNotFoundException;
import com.valchev.athletiq.domain.mapper.ExerciseHistoryMapper;
import com.valchev.athletiq.domain.mapper.SetHistoryMapper;
import com.valchev.athletiq.domain.mapper.WorkoutHistoryMapper;
import com.valchev.athletiq.repository.ExerciseHistoryRepository;
import com.valchev.athletiq.repository.SetHistoryRepository;
import com.valchev.athletiq.repository.WorkoutHistoryRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WorkoutHistoryService {

    private final WorkoutHistoryRepository workoutHistoryRepository;
    private final SetHistoryRepository setHistoryRepository;
    private final ExerciseHistoryRepository exerciseHistoryRepository;
    private final ExerciseHistoryMapper exerciseHistoryMapper;
    private final WorkoutHistoryMapper workoutHistoryMapper;
    private final SetHistoryMapper setHistoryMapper;

    @Autowired
    public WorkoutHistoryService(WorkoutHistoryRepository workoutHistoryRepository,
                                 SetHistoryRepository setHistoryRepository,
                                 ExerciseHistoryRepository exerciseHistoryRepository,
                                 ExerciseHistoryMapper exerciseHistoryMapper,
                                 WorkoutHistoryMapper workoutHistoryMapper,
                                 SetHistoryMapper setHistoryMapper) {
        this.workoutHistoryRepository = workoutHistoryRepository;
        this.setHistoryRepository = setHistoryRepository;
        this.exerciseHistoryRepository = exerciseHistoryRepository;
        this.exerciseHistoryMapper = exerciseHistoryMapper;
        this.workoutHistoryMapper = workoutHistoryMapper;
        this.setHistoryMapper = setHistoryMapper;
    }

    @Transactional
    public WorkoutHistory saveWorkoutToHistory(WorkoutHistoryDTO workoutHistoryDTO) {
        WorkoutHistory workoutHistory = workoutHistoryMapper.toEntity(workoutHistoryDTO);
        log.info("Saving workout history: {}", workoutHistory);
        if (workoutHistory.getExerciseHistories() != null) {
            for (ExerciseHistory exercise : workoutHistory.getExerciseHistories()) {
                exercise.setWorkoutHistory(workoutHistory);
                if (exercise.getExerciseSetHistories() != null) {
                    for (ExerciseSetHistory set : exercise.getExerciseSetHistories()) {
                        set.setExerciseHistory(exercise);
                    }
                }
            }
        }
        return workoutHistoryRepository.save(workoutHistory);
    }

    public WorkoutHistoryDTO findById(UUID workoutHistoryId) {
        WorkoutHistory workoutHistory = workoutHistoryRepository.findById(workoutHistoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout history not found with ID: " + workoutHistoryId));
        return workoutHistoryMapper.toDTO(workoutHistory);
    }

    public List<WorkoutHistoryDTO> findAllByUserId(UUID userId) {
        return workoutHistoryRepository.findAllByUserId(userId).stream()
                .map(workoutHistoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    public WorkoutStats calculateWorkoutStats(UUID userId) {
        // Get all workouts for this user
        List<WorkoutHistory> workouts = workoutHistoryRepository.findAllByUserId(userId);

        // Calculate total workouts
        int totalWorkouts = workouts.size();

        int hoursActive = calculateTotalHours(workouts);

        int uniqueDays = workoutHistoryRepository.countDistinctWorkoutDaysByUserId(userId);


        return WorkoutStats.builder()
                .totalWorkouts(totalWorkouts)
                .uniqueDays(uniqueDays)
                .hoursActive(hoursActive)
                .build();
    }


    private int calculateTotalHours(List<WorkoutHistory> workouts) {
        Duration totalDuration = workouts.stream()
                .map(WorkoutHistory::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);

        // Convert to hours and round to the nearest whole number
        long totalSeconds = totalDuration.getSeconds();
        return (int) Math.round(totalSeconds / 3600.0);
    }



}