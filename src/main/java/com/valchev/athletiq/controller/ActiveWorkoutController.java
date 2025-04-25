package com.valchev.athletiq.controller;

import com.valchev.athletiq.domain.dto.ActiveWorkoutDTO;
import com.valchev.athletiq.service.ActiveWorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/active-workouts")
@RequiredArgsConstructor
public class ActiveWorkoutController {

    private final ActiveWorkoutService activeWorkoutService;

    @PostMapping("/start")
    public ResponseEntity<ActiveWorkoutDTO> startWorkout(@RequestBody ActiveWorkoutDTO workoutDTO) {
        workoutDTO.setStartTime(OffsetDateTime.now());
        ActiveWorkoutDTO startedWorkout = activeWorkoutService.startWorkout(workoutDTO);
        return ResponseEntity.ok(startedWorkout);
    }

    @GetMapping("/current")
    public ResponseEntity<List<ActiveWorkoutDTO>> getCurrentWorkouts() {
        List<ActiveWorkoutDTO> activeWorkouts = activeWorkoutService.findActiveWorkouts();
        return ResponseEntity.ok(activeWorkouts);
    }

    @PostMapping("/{id}/finish")
    public ResponseEntity<ActiveWorkoutDTO> finishWorkout(@PathVariable UUID id) {
        ActiveWorkoutDTO finishedWorkout = activeWorkoutService.finishWorkout(id);
        return ResponseEntity.ok(finishedWorkout);
    }

    @PutMapping("/{id}/exercise")
    public ResponseEntity<ActiveWorkoutDTO> addExerciseToWorkout(
            @PathVariable UUID id,
            @RequestParam UUID exerciseId) {
        ActiveWorkoutDTO updatedWorkout = activeWorkoutService.addExercise(id, exerciseId);
        return ResponseEntity.ok(updatedWorkout);
    }
}