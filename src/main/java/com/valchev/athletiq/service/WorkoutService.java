package com.valchev.athletiq.service;

import com.valchev.athletiq.domain.dto.WorkoutDTO;
import com.valchev.athletiq.domain.entity.Workout;
import com.valchev.athletiq.domain.mapper.WorkoutMapper;
import com.valchev.athletiq.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final WorkoutMapper workoutMapper;
    private final ExerciseService exerciseService; // ✅ Inject ExerciseService

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository, WorkoutMapper workoutMapper, ExerciseService exerciseService) {
        this.workoutRepository = workoutRepository;
        this.workoutMapper = workoutMapper;
        this.exerciseService = exerciseService;
    }

    public List<WorkoutDTO> findAll() {
        List<Workout> workouts = workoutRepository.findAll();
        return workoutMapper.toDTOs(workouts);
    }

    public Optional<WorkoutDTO> findById(UUID workoutId) {
        return workoutRepository.findById(workoutId)
                .map(workoutMapper::toDTO);
    }

    public WorkoutDTO save(WorkoutDTO workoutDTO) {
        Workout workout = workoutMapper.toEntity(workoutDTO, exerciseService);
        Workout savedWorkout = workoutRepository.save(workout);
        return workoutMapper.toDTO(savedWorkout);
    }

    public void deleteById(UUID workoutId) {
        workoutRepository.deleteById(workoutId);
    }
}

