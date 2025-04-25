package com.valchev.athletiq.service;

import com.valchev.athletiq.domain.dto.ActiveWorkoutDTO;
import com.valchev.athletiq.domain.entity.ActiveWorkout;
import com.valchev.athletiq.domain.mapper.ActiveWorkoutMapper;
import com.valchev.athletiq.repository.ActiveWorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActiveWorkoutService {

    private final ActiveWorkoutRepository activeWorkoutRepository;
    private final ActiveWorkoutMapper workoutMapper;
    private final ExerciseService exerciseService;

    public ActiveWorkoutDTO startWorkout(ActiveWorkoutDTO workoutDTO) {
        ActiveWorkout workout = workoutMapper.toEntity(workoutDTO, exerciseService);
        workout.setStartTime(OffsetDateTime.now());
        ActiveWorkout savedWorkout = activeWorkoutRepository.save(workout);
        return workoutMapper.toDTO(savedWorkout);
    }

    public List<ActiveWorkoutDTO> findActiveWorkouts() {
        List<ActiveWorkout> activeWorkouts = activeWorkoutRepository.findByEndTimeIsNull();
        return activeWorkouts.stream()
                .map(workoutMapper::toDTO)
                .toList();
    }

    public ActiveWorkoutDTO finishWorkout(UUID id) {
        ActiveWorkout workout = activeWorkoutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Workout not found"));

        workout.setEndTime(OffsetDateTime.now());
        ActiveWorkout savedWorkout = activeWorkoutRepository.save(workout);
        return workoutMapper.toDTO(savedWorkout);
    }

    public ActiveWorkoutDTO addExercise(UUID workoutId, UUID exerciseId) {
        ActiveWorkout workout = activeWorkoutRepository.findById(workoutId)
                .orElseThrow(() -> new RuntimeException("Workout not found"));

        if (workout.getExercises() == null) {
            workout.setExercises(new ArrayList<>());
        }

        workout.getExercises().add(exerciseService.findEntityById(exerciseId));

        ActiveWorkout savedWorkout = activeWorkoutRepository.save(workout);
        return workoutMapper.toDTO(savedWorkout);
    }
}