package com.valchev.athletiq.service;

import com.valchev.athletiq.domain.entity.ExerciseSet;
import com.valchev.athletiq.repository.ExerciseSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExerciseSetService {

    private final ExerciseSetRepository exerciseSetRepository;

    @Autowired
    public ExerciseSetService(ExerciseSetRepository exerciseSetRepository) {
        this.exerciseSetRepository = exerciseSetRepository;
    }

    public Optional<ExerciseSet> findById(UUID id) {
        return exerciseSetRepository.findById(id);
    }

    public List<ExerciseSet> findAllByIds(List<UUID> ids) {
        return exerciseSetRepository.findAllById(ids);
    }
}