package com.valchev.athletiq.service;

import com.valchev.athletiq.domain.dto.ExerciseTemplateDTO;
import com.valchev.athletiq.domain.entity.ExerciseTemplate;
import com.valchev.athletiq.domain.mapper.ExerciseTemplateMapper;
import com.valchev.athletiq.repository.ExerciseTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExerciseTemplateService {

    private final ExerciseTemplateRepository exerciseTemplateRepository;
    private final ExerciseTemplateMapper exerciseTemplateMapper;

    public List<ExerciseTemplateDTO> findAll() {
        return exerciseTemplateMapper.toDTOList(exerciseTemplateRepository.findAll());
    }

    public Optional<ExerciseTemplateDTO> findById(UUID id) {
        return exerciseTemplateRepository.findById(id)
                .map(exerciseTemplateMapper::toDTO);
    }

    public Optional<ExerciseTemplateDTO> findByName(String name) {
        return exerciseTemplateRepository.findByNameIgnoreCase(name)
                .map(exerciseTemplateMapper::toDTO);
    }

    public List<ExerciseTemplateDTO> findByTargetMuscleGroup(String muscleGroup) {
        List<ExerciseTemplate> templates = exerciseTemplateRepository.findAll().stream()
                .filter(template -> template.getTargetMuscleGroups().contains(muscleGroup))
                .toList();

        return exerciseTemplateMapper.toDTOList(templates);
    }

    public Optional<String> findImageUrlByName(String exerciseName) {
        return exerciseTemplateRepository.findByNameIgnoreCase(exerciseName)
                .map(ExerciseTemplate::getImageUrl);
    }
}