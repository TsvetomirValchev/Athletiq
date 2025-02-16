package com.valchev.athletiq.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valchev.athletiq.domain.entity.Exercise;
import com.valchev.athletiq.repository.ExerciseRepository;

@Service
public class ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;

    public List<Exercise> findAll() {
        return exerciseRepository.findAll();
    }

    public Optional<Exercise> findById(UUID exerciseId) {
        return exerciseRepository.findById(exerciseId);
    }

    public Exercise save(Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    public void deleteById(UUID exerciseId) {
        exerciseRepository.deleteById(exerciseId);
    }

}
