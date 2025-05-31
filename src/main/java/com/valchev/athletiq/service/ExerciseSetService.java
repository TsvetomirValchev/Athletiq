package com.valchev.athletiq.service;

import com.valchev.athletiq.domain.dto.ExerciseSetDTO;
import com.valchev.athletiq.domain.entity.ExerciseSet;
import com.valchev.athletiq.domain.exception.ResourceNotFoundException;
import com.valchev.athletiq.domain.mapper.ExerciseSetMapper;
import com.valchev.athletiq.repository.ExerciseSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ExerciseSetService {

    private final ExerciseSetRepository exerciseSetRepository;
    private final ExerciseSetMapper exerciseSetMapper;

    @Autowired
    public ExerciseSetService(ExerciseSetRepository exerciseSetRepository, ExerciseSetMapper exerciseSetMapper) {
        this.exerciseSetRepository = exerciseSetRepository;
        this.exerciseSetMapper = exerciseSetMapper;
    }

    public ExerciseSetDTO save(ExerciseSetDTO exerciseSetDTO) {
        ExerciseSet exerciseSet = exerciseSetMapper.toEntity(exerciseSetDTO);
        ExerciseSet savedExerciseSet = exerciseSetRepository.save(exerciseSet);
        return exerciseSetMapper.toDTO(savedExerciseSet);
    }

    public void deleteByOrderPosition(int orderPosition, UUID exerciseId) {
        ExerciseSet exerciseSet = exerciseSetRepository.findByOrderPositionAndExercise_ExerciseId(orderPosition, exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("ExerciseSet not found with order position: " + orderPosition));
        exerciseSetRepository.delete(exerciseSet);
    }

    public List<ExerciseSetDTO> getExerciseSetsByExerciseId(UUID exerciseId) {
        return exerciseSetRepository.findAllByExercise_ExerciseId(exerciseId)
                .stream()
                .map(exerciseSetMapper::toDTO)
                .toList();
    }


    public ExerciseSetDTO updateExerciseSet(UUID exerciseId, ExerciseSetDTO exerciseSetDTO) {
        ExerciseSet existingExercise = exerciseSetRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("ExerciseSet not found with ID: " + exerciseId));

        ExerciseSet updatedExercise = exerciseSetMapper.toEntity(exerciseSetDTO);
        exerciseSetMapper.update(existingExercise, updatedExercise);

        ExerciseSet savedExercise = exerciseSetRepository.save(existingExercise);
        return exerciseSetMapper.toDTO(savedExercise);
    }
}
