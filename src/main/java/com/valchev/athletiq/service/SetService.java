package com.valchev.athletiq.service;

import com.valchev.athletiq.domain.dto.SetDTO;
import com.valchev.athletiq.domain.entity.ExerciseSet;
import com.valchev.athletiq.domain.exception.ResourceNotFoundException;
import com.valchev.athletiq.mapper.SetMapper;
import com.valchev.athletiq.repository.SetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SetService {

    private final SetRepository setRepository;
    private final SetMapper setMapper;

    @Autowired
    public SetService(SetRepository setRepository, SetMapper setMapper) {
        this.setRepository = setRepository;
        this.setMapper = setMapper;
    }

    public SetDTO save(SetDTO setDTO) {
        ExerciseSet exerciseSet = setMapper.toEntity(setDTO);
        ExerciseSet savedExerciseSet = setRepository.save(exerciseSet);
        return setMapper.toDTO(savedExerciseSet);
    }

    public void deleteByOrderPosition(int orderPosition, UUID exerciseId) {
        ExerciseSet exerciseSet = setRepository.findByOrderPositionAndExercise_ExerciseId(orderPosition, exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("ExerciseSet not found with order position: " + orderPosition));
        setRepository.delete(exerciseSet);
    }

    public List<SetDTO> getExerciseSetsByExerciseId(UUID exerciseId) {
        return setRepository.findAllByExercise_ExerciseId(exerciseId)
                .stream()
                .map(setMapper::toDTO)
                .toList();
    }


    public SetDTO updateExerciseSet(UUID exerciseId, SetDTO setDTO) {
        ExerciseSet existingExercise = setRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("ExerciseSet not found with ID: " + exerciseId));

        ExerciseSet updatedExercise = setMapper.toEntity(setDTO);
        setMapper.update(existingExercise, updatedExercise);

        ExerciseSet savedExercise = setRepository.save(existingExercise);
        return setMapper.toDTO(savedExercise);
    }
}
