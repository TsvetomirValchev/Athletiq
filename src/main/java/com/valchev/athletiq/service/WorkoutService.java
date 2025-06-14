package com.valchev.athletiq.service;

import com.valchev.athletiq.domain.dto.WorkoutDTO;
import com.valchev.athletiq.domain.entity.Workout;
import com.valchev.athletiq.domain.exception.AccessDeniedException;
import com.valchev.athletiq.domain.exception.ResourceNotFoundException;
import com.valchev.athletiq.mapper.WorkoutMapper;
import com.valchev.athletiq.repository.WorkoutRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final WorkoutMapper workoutMapper;
    private final ExerciseService exerciseService;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository, WorkoutMapper workoutMapper,
                          ExerciseService exerciseService) {
        this.workoutRepository = workoutRepository;
        this.workoutMapper = workoutMapper;
        this.exerciseService = exerciseService;
    }

    public Optional<WorkoutDTO> findById(UUID workoutId) {
        return workoutRepository.findById(workoutId)
                .map(workoutMapper::toDTO);
    }

    public WorkoutDTO updateWorkout(UUID workoutId, WorkoutDTO workoutDTO) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with ID: " + workoutId));

        workout.setName(workoutDTO.getName());

        Workout savedWorkout = workoutRepository.save(workout);
        return workoutMapper.toDTO(savedWorkout);
    }

    public WorkoutDTO save(WorkoutDTO workoutDTO) {
        Workout workout = workoutMapper.toEntity(workoutDTO, exerciseService);
        Workout savedWorkout = workoutRepository.save(workout);
        return workoutMapper.toDTO(savedWorkout);
    }

    public void deleteById(UUID workoutId) {
        workoutRepository.deleteById(workoutId);
    }

    public List<WorkoutDTO> findAllByUserId(UUID userId) {
        return workoutRepository.findByUser_UserId(userId).stream()
                .map(workoutMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void verifyOwnership(UUID workoutId, UUID userId) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with ID: " + workoutId));

        if (!workout.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("You don't have permission to access this workout");
        }
    }
}