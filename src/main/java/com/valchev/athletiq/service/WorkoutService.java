package com.valchev.athletiq.service;

import com.valchev.athletiq.domain.dto.ExerciseDTO;
import com.valchev.athletiq.domain.dto.ExerciseSetDTO;
import com.valchev.athletiq.domain.dto.WorkoutDTO;
import com.valchev.athletiq.domain.entity.Exercise;
import com.valchev.athletiq.domain.entity.Workout;
import com.valchev.athletiq.domain.exception.AccessDeniedException;
import com.valchev.athletiq.domain.exception.ResourceNotFoundException;
import com.valchev.athletiq.domain.mapper.ExerciseMapper;
import com.valchev.athletiq.domain.mapper.ExerciseSetMapper;
import com.valchev.athletiq.domain.mapper.WorkoutMapper;
import com.valchev.athletiq.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final WorkoutMapper workoutMapper;
    private final ExerciseService exerciseService;
    private final ExerciseMapper exerciseMapper;
    private final ExerciseSetMapper exerciseSetMapper;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository, WorkoutMapper workoutMapper,
                          ExerciseService exerciseService, ExerciseMapper exerciseMapper,
                          ExerciseSetMapper exerciseSetMapper) {
        this.workoutRepository = workoutRepository;
        this.workoutMapper = workoutMapper;
        this.exerciseService = exerciseService;
        this.exerciseMapper = exerciseMapper;
        this.exerciseSetMapper = exerciseSetMapper;
    }

    private static void checkIfExerciseExistsInWorkout(UUID exerciseId, Workout workout) {
        boolean exerciseExists = workout.getExercises().stream()
                .anyMatch(e -> e.getExerciseId().equals(exerciseId));

        if (!exerciseExists) {
            throw new ResourceNotFoundException("Exercise not found in workout");
        }
    }

    public List<WorkoutDTO> findAll() {
        List<Workout> workouts = workoutRepository.findAll();
        return workoutMapper.toDTOs(workouts);
    }

    public Optional<WorkoutDTO> findById(UUID workoutId) {
        return workoutRepository.findById(workoutId)
                .map(workoutMapper::toDTO);
    }

    public WorkoutDTO save(WorkoutDTO workoutDTO) {
        Workout workout = workoutMapper.toEntity(workoutDTO, exerciseService);
        Workout savedWorkout = workoutRepository.save(workout);
        return workoutMapper.toDTO(savedWorkout);
    }

    public void deleteById(UUID workoutId) {
        workoutRepository.deleteById(workoutId);
    }

    public List<ExerciseDTO> getExercisesByWorkoutId(UUID workoutId) {
        Workout workout = retrieveWorkout(workoutId);
        return workout.getExercises().stream()
                .map(exerciseMapper::toDTO)
                .collect(Collectors.toList());
    }

    public WorkoutDTO addExerciseToWorkout(UUID workoutId, ExerciseDTO exerciseDTO) {
        Workout workout = retrieveWorkout(workoutId);

        Exercise exercise = exerciseMapper.toEntity(exerciseDTO);
        exercise.setWorkout(workout);

        if (workout.getExercises() == null) {
            workout.setExercises(new ArrayList<>());
        }

        workout.getExercises().add(exercise);
        Workout savedWorkout = workoutRepository.save(workout);

        return workoutMapper.toDTO(savedWorkout);
    }

    public ExerciseDTO getWorkoutExerciseById(UUID workoutId, UUID exerciseId) {
        Workout workout = retrieveWorkout(workoutId);

        Exercise exercise = workout.getExercises().stream()
                .filter(e -> e.getExerciseId().equals(exerciseId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found in workout"));

        return exerciseMapper.toDTO(exercise);
    }

    public WorkoutDTO updateWorkoutExercise(UUID workoutId, ExerciseDTO exerciseDTO) {
        Workout workout = retrieveWorkout(workoutId);

        boolean exerciseUpdated = false;
        for (int i = 0; i < workout.getExercises().size(); i++) {
            if (workout.getExercises().get(i).getExerciseId().equals(exerciseDTO.getExerciseId())) {
                Exercise updatedExercise = exerciseMapper.toEntity(exerciseDTO);
                updatedExercise.setWorkout(workout);
                workout.getExercises().set(i, updatedExercise);
                exerciseUpdated = true;
                break;
            }
        }

        if (!exerciseUpdated) {
            throw new ResourceNotFoundException("Exercise not found in workout");
        }

        Workout savedWorkout = workoutRepository.save(workout);
        return workoutMapper.toDTO(savedWorkout);
    }

    public WorkoutDTO removeExerciseFromWorkout(UUID workoutId, UUID exerciseId) {
        Workout workout = retrieveWorkout(workoutId);

        boolean removed = workout.getExercises().removeIf(e -> e.getExerciseId().equals(exerciseId));

        if (!removed) {
            throw new ResourceNotFoundException("Exercise not found in workout");
        }

        Workout savedWorkout = workoutRepository.save(workout);
        return workoutMapper.toDTO(savedWorkout);
    }


    public List<WorkoutDTO> findAllByUserId(UUID userId) {
        return workoutRepository.findByUser_UserId(userId).stream()
                .map(workoutMapper::toDTO)
                .collect(Collectors.toList());
    }



    public void verifyOwnership(UUID workoutId, UUID userId) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with ID: " + workoutId));

        if (!workout.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("You don't have permission to access this workout");
        }
    }

    public List<ExerciseSetDTO> getExerciseSets(UUID workoutId, UUID exerciseId) {
        Workout workout = retrieveWorkout(workoutId);

        Exercise exercise = workout.getExercises().stream()
                .filter(e -> e.getExerciseId().equals(exerciseId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found in workout"));

        return exercise.getSets().stream()
                .map(exerciseSetMapper::toDTO)
                .collect(Collectors.toList());
    }

    public WorkoutDTO addSetToExercise(UUID workoutId, UUID exerciseId, ExerciseSetDTO setDTO) {
        Workout workout = retrieveWorkout(workoutId);

        checkIfExerciseExistsInWorkout(exerciseId, workout);

        exerciseService.addSetToExercise(exerciseId, setDTO);

        Workout updatedWorkout = retrieveWorkout(workoutId);

        return workoutMapper.toDTO(updatedWorkout);
    }

    public WorkoutDTO completeExerciseSet(UUID workoutId, UUID exerciseId, UUID setId) {
        Workout workout = retrieveWorkout(workoutId);

        checkIfExerciseExistsInWorkout(exerciseId, workout);

        exerciseService.completeSet(exerciseId, setId);

        // Re-fetch the updated workout
        Workout updatedWorkout = retrieveWorkout(workoutId);

        return workoutMapper.toDTO(updatedWorkout);
    }

    public WorkoutDTO removeSetFromExercise(UUID workoutId, UUID exerciseId, Integer orderPosition) {
        Workout workout = retrieveWorkout(workoutId);

        checkIfExerciseExistsInWorkout(exerciseId, workout);

        exerciseService.removeSetByOrderPosition(exerciseId, orderPosition);

        // Re-fetch the updated workout
        Workout updatedWorkout = retrieveWorkout(workoutId);

        return workoutMapper.toDTO(updatedWorkout);
    }

    private Workout retrieveWorkout(UUID workoutId) {
        return workoutRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Active workout not found"));
    }
}