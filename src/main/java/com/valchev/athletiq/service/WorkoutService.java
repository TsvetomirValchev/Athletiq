package com.valchev.athletiq.service;

import com.valchev.athletiq.domain.entity.Workout;
import com.valchev.athletiq.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WorkoutService {

    @Autowired
    private WorkoutRepository workoutRepository;

    public List<Workout> findAll() {
        return workoutRepository.findAll();
    }

    public Optional<Workout> findById(UUID workoutId) {
        return workoutRepository.findById(workoutId);
    }

    public Workout save(Workout workout) {
        return workoutRepository.save(workout);
    }

    public void deleteById(UUID workoutId) {
        workoutRepository.deleteById(workoutId);
    }
}
