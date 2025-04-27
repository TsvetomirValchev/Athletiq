package com.valchev.athletiq.service;

import com.valchev.athletiq.domain.dto.ActiveWorkoutDTO;
import com.valchev.athletiq.domain.dto.ExerciseDTO;
import com.valchev.athletiq.domain.dto.ExerciseSetDTO;
import com.valchev.athletiq.domain.entity.ActiveWorkout;
import com.valchev.athletiq.domain.entity.Exercise;
import com.valchev.athletiq.domain.exception.AccessDeniedException;
import com.valchev.athletiq.domain.exception.ResourceNotFoundException;
import com.valchev.athletiq.domain.mapper.ActiveWorkoutMapper;
import com.valchev.athletiq.domain.mapper.ExerciseMapper;
import com.valchev.athletiq.domain.mapper.ExerciseSetMapper;
import com.valchev.athletiq.repository.ActiveWorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActiveWorkoutService {

    private final ActiveWorkoutRepository activeWorkoutRepository;
    private final ActiveWorkoutMapper workoutMapper;
    private final ExerciseService exerciseService;
    private final ExerciseMapper exerciseMapper;
    private final ExerciseSetMapper exerciseSetMapper;


    public ActiveWorkoutDTO startWorkout(ActiveWorkoutDTO workoutDTO) {
        ActiveWorkout workout = workoutMapper.toEntity(workoutDTO, exerciseService);
        workout.setStartTime(OffsetDateTime.now());
        ActiveWorkout savedWorkout = activeWorkoutRepository.save(workout);
        return workoutMapper.toDTO(savedWorkout);
    }

    public ActiveWorkoutDTO finishWorkout(UUID id) {
        ActiveWorkout workout = activeWorkoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found"));

        workout.setEndTime(OffsetDateTime.now());
        ActiveWorkout savedWorkout = activeWorkoutRepository.save(workout);
        return workoutMapper.toDTO(savedWorkout);
    }

    public List<ExerciseDTO> getExercisesByWorkoutId(UUID workoutId) {
        ActiveWorkout workout = retrieveActiveWorkout(workoutId);
        return workout.getExercises().stream()
                .map(exerciseMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ActiveWorkoutDTO addExerciseToWorkout(UUID workoutId, ExerciseDTO exerciseDTO) {
        ActiveWorkout workout = retrieveActiveWorkout(workoutId);

        Exercise exercise = exerciseMapper.toEntity(exerciseDTO);

        if (workout.getExercises() == null) {
            workout.setExercises(new ArrayList<>());
        }

        workout.getExercises().add(exercise);
        ActiveWorkout savedWorkout = activeWorkoutRepository.save(workout);

        return workoutMapper.toDTO(savedWorkout);
    }

    public List<ActiveWorkoutDTO> findActiveWorkoutsByUserId(UUID userId) {
        return activeWorkoutRepository.findByUser_UserIdAndEndTimeIsNull(userId).stream()
                .map(workoutMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void verifyOwnership(UUID workoutId, UUID userId) {
        ActiveWorkout workout = activeWorkoutRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Active workout not found with ID: " + workoutId));

        if (!workout.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("You don't have permission to access this workout");
        }
    }

    public ExerciseDTO getWorkoutExerciseById(UUID workoutId, UUID exerciseId) {
        ActiveWorkout workout = retrieveActiveWorkout(workoutId);

        Exercise exercise = workout.getExercises().stream()
                .filter(e -> e.getExerciseId().equals(exerciseId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found in workout"));

        return exerciseMapper.toDTO(exercise);
    }

    public ActiveWorkoutDTO updateWorkoutExercise(UUID workoutId, ExerciseDTO exerciseDTO) {
        ActiveWorkout workout = retrieveActiveWorkout(workoutId);

        boolean exerciseUpdated = false;
        for (int i = 0; i < workout.getExercises().size(); i++) {
            if (workout.getExercises().get(i).getExerciseId().equals(exerciseDTO.getExerciseId())) {
                Exercise updatedExercise = exerciseMapper.toEntity(exerciseDTO);
                workout.getExercises().set(i, updatedExercise);
                exerciseUpdated = true;
                break;
            }
        }

        if (!exerciseUpdated) {
            throw new ResourceNotFoundException("Exercise not found in workout");
        }

        ActiveWorkout savedWorkout = activeWorkoutRepository.save(workout);
        return workoutMapper.toDTO(savedWorkout);
    }

    public ActiveWorkoutDTO removeExerciseFromWorkout(UUID workoutId, UUID exerciseId) {
        ActiveWorkout workout = retrieveActiveWorkout(workoutId);

        boolean removed = workout.getExercises().removeIf(e -> e.getExerciseId().equals(exerciseId));

        if (!removed) {
            throw new ResourceNotFoundException("Exercise not found in workout");
        }

        ActiveWorkout savedWorkout = activeWorkoutRepository.save(workout);
        return workoutMapper.toDTO(savedWorkout);
    }

    public List<ExerciseSetDTO> getExerciseSets(UUID workoutId, UUID exerciseId) {
        ActiveWorkout workout = retrieveActiveWorkout(workoutId);

        Exercise exercise = workout.getExercises().stream()
                .filter(e -> e.getExerciseId().equals(exerciseId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found in workout"));

        return exercise.getSets().stream()
                .map(exerciseSetMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ActiveWorkoutDTO addSetToExercise(UUID workoutId, UUID exerciseId, ExerciseSetDTO exerciseSetDTO) {
        ActiveWorkout workout = retrieveActiveWorkout(workoutId);

        checkIfExerciseExistsInWorkout(exerciseId, workout);

        exerciseSetDTO.setCompleted(false);

        exerciseService.addSetToExercise(exerciseId, exerciseSetDTO);

        ActiveWorkout savedWorkout = activeWorkoutRepository.save(workout);
        return workoutMapper.toDTO(savedWorkout);
    }

    public ActiveWorkoutDTO completeSet(UUID workoutId, UUID exerciseId, UUID setId) {
        ActiveWorkout workout = retrieveActiveWorkout(workoutId);

        checkIfExerciseExistsInWorkout(exerciseId, workout);

        exerciseService.completeSet(exerciseId, setId);

        ActiveWorkout savedWorkout = activeWorkoutRepository.save(workout);
        return workoutMapper.toDTO(savedWorkout);
    }

    public ActiveWorkoutDTO removeSetFromExercise(UUID workoutId, UUID exerciseId, Integer orderPosition) {
        ActiveWorkout workout = retrieveActiveWorkout(workoutId);

        checkIfExerciseExistsInWorkout(exerciseId, workout);

        exerciseService.removeSetByOrderPosition(exerciseId, orderPosition);

        ActiveWorkout updatedWorkout = retrieveActiveWorkout(workoutId);

        return workoutMapper.toDTO(updatedWorkout);
    }

    private ActiveWorkout retrieveActiveWorkout(UUID workoutId) {
        return activeWorkoutRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Active workout not found"));
    }

    private void checkIfExerciseExistsInWorkout(UUID exerciseId, ActiveWorkout workout) {
        boolean exerciseExists = workout.getExercises().stream()
                .anyMatch(e -> e.getExerciseId().equals(exerciseId));

        if (!exerciseExists) {
            throw new ResourceNotFoundException("Exercise not found in workout");
        }
    }
}