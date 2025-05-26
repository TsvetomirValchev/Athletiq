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
    private final ExerciseSetMapper exerciseSetMapper;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository, ExerciseSetMapper exerciseSetMapper) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseSetMapper = exerciseSetMapper;
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

    public void addSetToExercise(UUID exerciseId, ExerciseSetDTO setDTO) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));

        setDTO.setExerciseId(exerciseId);

        ExerciseSet set = exerciseSetMapper.toEntity(setDTO);

        exercise.getSets().add(set);

        exerciseRepository.save(exercise);
    }
}
