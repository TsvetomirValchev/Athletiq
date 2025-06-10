package com.valchev.athletiq.service;

import com.valchev.athletiq.domain.dto.WorkoutHistoryDTO;
import com.valchev.athletiq.domain.dto.WorkoutStatsDTO;
import com.valchev.athletiq.domain.entity.ExerciseHistory;
import com.valchev.athletiq.domain.entity.SetHistory;
import com.valchev.athletiq.domain.entity.WorkoutHistory;
import com.valchev.athletiq.domain.exception.ResourceNotFoundException;
import com.valchev.athletiq.mapper.WorkoutHistoryMapper;
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
    private final WorkoutHistoryMapper workoutHistoryMapper;

    @Autowired
    public WorkoutHistoryService(WorkoutHistoryRepository workoutHistoryRepository,
                                 WorkoutHistoryMapper workoutHistoryMapper) {
        this.workoutHistoryRepository = workoutHistoryRepository;
        this.workoutHistoryMapper = workoutHistoryMapper;
    }

    @Transactional
    public WorkoutHistory saveWorkoutToHistory(WorkoutHistoryDTO workoutHistoryDTO) {
        WorkoutHistory workoutHistory = workoutHistoryMapper.toEntity(workoutHistoryDTO);
        log.info("Saving workout history: {}", workoutHistory);
        if (workoutHistory.getExerciseHistories() != null) {
            for (ExerciseHistory exercise : workoutHistory.getExerciseHistories()) {
                exercise.setWorkoutHistory(workoutHistory);
                if (exercise.getExerciseSetHistories() != null) {
                    for (SetHistory set : exercise.getExerciseSetHistories()) {
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

    public WorkoutStatsDTO calculateWorkoutStats(UUID userId) {
        List<WorkoutHistory> workouts = workoutHistoryRepository.findAllByUserId(userId);

        int totalWorkouts = workouts.size();

        int hoursActive = calculateTotalHours(workouts);

        int uniqueDays = workoutHistoryRepository.countDistinctWorkoutDaysByUserId(userId);


        return WorkoutStatsDTO.builder()
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

        long totalSeconds = totalDuration.getSeconds();
        return (int) Math.round(totalSeconds / 3600.0);
    }



}