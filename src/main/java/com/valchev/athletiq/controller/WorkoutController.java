package com.valchev.athletiq.controller;

import com.valchev.athletiq.domain.dto.WorkoutDTO;
import com.valchev.athletiq.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valchev.athletiq.domain.entity.Workout;
import com.valchev.athletiq.service.WorkoutService;

@RestController
@RequestMapping("/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    @Autowired
    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @GetMapping
    public List<WorkoutDTO> getAllWorkouts() {
        return workoutService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutDTO> getWorkoutById(@PathVariable UUID id) {
        Optional<WorkoutDTO> workoutDTO = workoutService.findById(id);
        return workoutDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public WorkoutDTO createWorkout(@RequestBody WorkoutDTO workoutDTO) {
        return workoutService.save(workoutDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable UUID id) {
        workoutService.deleteById(id);
        return ResponseEntity.noContent()
                .build();
    }

}
