package com.valchev.athletiq.service;

import com.valchev.athletiq.domain.dto.ExerciseDTO;
import com.valchev.athletiq.domain.dto.ExerciseSetDTO;
import com.valchev.athletiq.domain.dto.WorkoutDTO;
import com.valchev.athletiq.domain.entity.Exercise;
import com.valchev.athletiq.domain.entity.ExerciseSet;
import com.valchev.athletiq.domain.entity.Workout;
import com.valchev.athletiq.domain.exception.AccessDeniedException;
import com.valchev.athletiq.domain.exception.ResourceNotFoundException;
import com.valchev.athletiq.domain.mapper.ExerciseMapper;
import com.valchev.athletiq.domain.mapper.ExerciseSetMapper;
import com.valchev.athletiq.domain.mapper.WorkoutMapper;
import com.valchev.athletiq.repository.WorkoutRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
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

    public Optional<WorkoutDTO> findById(UUID workoutId) {
        return workoutRepository.findById(workoutId)
                .map(workoutMapper::toDTO);
    }

    public WorkoutDTO updateWorkout(UUID workoutId, WorkoutDTO workoutDTO) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with ID: " + workoutId));

        workout.setName(workoutDTO.getName());

        Workout savedWorkout = workoutRepository.save(workout);
        return workoutMapper.toDTO(savedWorkout);
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

    public WorkoutDTO addExerciseWithSets(UUID workoutId, ExerciseDTO exerciseDTO) {
        Workout workout = retrieveWorkout(workoutId);

        Exercise exercise = exerciseMapper.toEntity(exerciseDTO);
        exercise.setWorkout(workout);

        workout.getExercises().add(exercise);
        Workout savedWorkout = workoutRepository.save(workout);

        return workoutMapper.toDTO(savedWorkout);
    }

    public WorkoutDTO addBatchExercisesWithSets(UUID workoutId, List<ExerciseDTO> exerciseDTOs) {
        Workout workout = retrieveWorkout(workoutId);

        List<Exercise> exercises = exerciseDTOs.stream()
                .map(dto -> {
                    Exercise exercise = exerciseMapper.toEntity(dto);
                    exercise.setWorkout(workout);
                    return exercise;
                })
                .toList();

        workout.getExercises().addAll(exercises);
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

        Exercise existingExercise = workout.getExercises().stream().filter(
                        exercise -> exercise.getExerciseId().equals(exerciseDTO.getExerciseId())
                ).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found in workout"));

        // Basic exercise update with properties excluding sets
        Exercise tempExercise = exerciseMapper.toEntity(exerciseDTO);
        exerciseMapper.update(existingExercise, tempExercise);
        existingExercise.setWorkout(workout);

        // Handle sets separately only if provided in the DTO
        if (exerciseDTO.getSets() != null && !exerciseDTO.getSets().isEmpty()) {
            // Clear existing sets while maintaining the collection instance
            existingExercise.getSets().clear();

            // Add new sets from the DTO
            exerciseDTO.getSets().forEach(setDTO -> {
                ExerciseSet set = new ExerciseSet();
                set.setOrderPosition(setDTO.getOrderPosition());
                set.setReps(setDTO.getReps());
                set.setWeight(setDTO.getWeight());
                set.setRestTimeSeconds(setDTO.getRestTimeSeconds());
                set.setType(setDTO.getType());
                set.setExercise(existingExercise); // Set parent reference
                existingExercise.getSets().add(set); // Add to the collection
            });
        }

        Workout savedWorkout = workoutRepository.save(workout);
        return workoutMapper.toDTO(savedWorkout);
    }

    public WorkoutDTO removeExerciseFromWorkout(UUID workoutId, UUID exerciseId) {
        Workout workout = retrieveWorkout(workoutId);

        boolean removed = workout.getExercises().removeIf(e -> e.getExerciseId().equals(exerciseId));
        log.info("Removed value {}", removed);
        exerciseService.deleteById(exerciseId);

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

        log.info("Exercise is null ? {}", exercise == null);
        log.info("Exercise: {}", exercise.getExerciseId());
        log.info("Exercise Sets: {}", exercise.getSets());
        log.info("Exercise Sets after Mapper: {}", exercise.getSets().stream()
                .map(exerciseSetMapper::toDTO)
                .collect(Collectors.toList()));

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

    public WorkoutDTO removeSetFromExercise(UUID workoutId, UUID exerciseId, Integer orderPosition) {
        Workout workout = retrieveWorkout(workoutId);

        checkIfExerciseExistsInWorkout(exerciseId, workout);

        exerciseService.removeSetByOrderPosition(exerciseId, orderPosition);

        Workout updatedWorkout = retrieveWorkout(workoutId);

        return workoutMapper.toDTO(updatedWorkout);
    }

    private Workout retrieveWorkout(UUID workoutId) {
        return workoutRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Active workout not found"));
    }
}