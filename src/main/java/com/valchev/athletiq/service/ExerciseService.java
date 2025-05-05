package com.valchev.athletiq.service;

import com.valchev.athletiq.domain.dto.ExerciseDTO;
import com.valchev.athletiq.domain.dto.ExerciseSetDTO;
import com.valchev.athletiq.domain.entity.Exercise;
import com.valchev.athletiq.domain.entity.ExerciseSet;
import com.valchev.athletiq.domain.entity.Workout;
import com.valchev.athletiq.domain.exception.ResourceNotFoundException;
import com.valchev.athletiq.domain.mapper.ExerciseMapper;
import com.valchev.athletiq.domain.mapper.ExerciseSetMapper;
import com.valchev.athletiq.repository.ExerciseRepository;
import com.valchev.athletiq.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final WorkoutRepository workoutRepository;
    private final ExerciseMapper exerciseMapper;
    private final ExerciseSetMapper exerciseSetMapper;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository, WorkoutRepository workoutRepository, ExerciseMapper exerciseMapper, ExerciseSetMapper exerciseSetMapper, ExerciseSetService exerciseSetService) {
        this.exerciseRepository = exerciseRepository;
        this.workoutRepository = workoutRepository;
        this.exerciseMapper = exerciseMapper;
        this.exerciseSetMapper = exerciseSetMapper;
    }

    public List<ExerciseDTO> findAll() {
        List<Exercise> exercises = exerciseRepository.findAll();
        return exerciseMapper.toDTOs(exercises);
    }

    public Optional<ExerciseDTO> findById(UUID exerciseId) {
        return exerciseRepository.findById(exerciseId).map(exerciseMapper::toDTO);
    }

    public ExerciseDTO save(ExerciseDTO exerciseDTO) {
        Exercise exercise = exerciseMapper.toEntity(exerciseDTO);

        if (exerciseDTO.getWorkoutId() != null) {
            Optional<Workout> workout = workoutRepository.findById(exerciseDTO.getWorkoutId());
            workout.ifPresent(exercise::setWorkout);
        }

        Exercise savedExercise = exerciseRepository.save(exercise);
        return exerciseMapper.toDTO(savedExercise);
    }

    public void removeSetByOrderPosition(UUID exerciseId, Integer orderPosition) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));

        ExerciseSet setToRemove = exercise.getSets().stream()
                .filter(s -> s.getOrderPosition().equals(orderPosition))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Set not found"));

        exercise.removeSet(setToRemove);

        int newPosition = 1;
        for (ExerciseSet set : exercise.getSets()) {
            set.setOrderPosition(newPosition++);
        }

        exerciseRepository.save(exercise);
    }

    public List<Exercise> getExercisesByIds(List<UUID> exerciseIds) {
        return exerciseIds.stream()
                .map(exerciseRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public void addSetToExercise(UUID exerciseId, ExerciseSetDTO setDTO) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));

        setDTO.setExerciseId(exerciseId);

        ExerciseSet set = exerciseSetMapper.toEntity(setDTO);

        exercise.getSets().add(set);

        exerciseRepository.save(exercise);
    }

    public void completeSet(UUID exerciseId, UUID setId) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));

        ExerciseSet set = exercise.getSets().stream()
                .filter(s -> s.getExerciseSetId().equals(setId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Set not found"));

        set.setCompleted(true);

        exerciseRepository.save(exercise);
    }
}
