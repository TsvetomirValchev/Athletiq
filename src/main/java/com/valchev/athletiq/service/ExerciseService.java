package com.valchev.athletiq.service;

import com.valchev.athletiq.domain.dto.ExerciseDTO;
import com.valchev.athletiq.domain.entity.Exercise;
import com.valchev.athletiq.domain.entity.ExerciseSet;
import com.valchev.athletiq.domain.exception.ResourceNotFoundException;
import com.valchev.athletiq.mapper.ExerciseMapper;
import com.valchev.athletiq.mapper.SetMapper;
import com.valchev.athletiq.repository.ExerciseRepository;
import com.valchev.athletiq.repository.ExerciseTemplateRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;
    private final ExerciseTemplateRepository exerciseTemplateRepository;


    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository, ExerciseMapper exerciseMapper, SetMapper setMapper, ExerciseTemplateRepository exerciseTemplateRepository) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseMapper = exerciseMapper;
        this.exerciseTemplateRepository = exerciseTemplateRepository;
    }

    @Transactional
    public ExerciseDTO save(ExerciseDTO exerciseDTO) {
        Exercise exercise = exerciseMapper.toEntity(exerciseDTO);
        exercise.setExerciseTemplate(exerciseTemplateRepository.getReferenceById(exerciseDTO.getExerciseTemplateId()));
        Exercise savedExercise = exerciseRepository.save(exercise);
        return exerciseMapper.toDTO(savedExercise);
    }


    public void deleteById(UUID exerciseId) {
        exerciseRepository.deleteById(exerciseId);
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

    public ExerciseDTO updateExercise(UUID exerciseId, ExerciseDTO exerciseDTO) {
        Exercise existingExercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found with ID: " + exerciseId));

        existingExercise.setExerciseTemplate(exerciseTemplateRepository.getReferenceById(exerciseDTO.getExerciseTemplateId()));
        exerciseMapper.update(existingExercise, exerciseDTO);

        Exercise savedExercise = exerciseRepository.save(existingExercise);
        return exerciseMapper.toDTO(savedExercise);
    }

    public List<ExerciseDTO> getAllExercisesByWorkoutId(UUID workoutId) {
        return exerciseRepository.findAllExercisesByWorkout_WorkoutId(workoutId)
                .stream()
                .map(exerciseMapper::toDTO)
                .toList();
    }

    public ExerciseDTO findById(UUID exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found with ID: " + exerciseId));
        return exerciseMapper.toDTO(exercise);
    }

}
