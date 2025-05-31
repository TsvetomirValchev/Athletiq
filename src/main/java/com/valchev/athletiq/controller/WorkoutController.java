package com.valchev.athletiq.controller;

import com.valchev.athletiq.domain.dto.WorkoutDTO;
import com.valchev.athletiq.security.AthletiqUser;
import com.valchev.athletiq.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;

    @GetMapping
    public ResponseEntity<List<WorkoutDTO>> getAllWorkouts(Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        return ResponseEntity.ok(workoutService.findAllByUserId(user.getUserId()));
    }

    @GetMapping("/{workoutId}")
    public ResponseEntity<WorkoutDTO> getWorkout(
            @PathVariable UUID workoutId,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutService.verifyOwnership(workoutId, user.getUserId());
        return workoutService.findById(workoutId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<WorkoutDTO> createWorkout(
            @RequestBody WorkoutDTO workoutDTO,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutDTO.setUserId(user.getUserId());
        return ResponseEntity.ok(workoutService.save(workoutDTO));
    }

    @PatchMapping("/{workoutId}")
    public ResponseEntity<WorkoutDTO> patchWorkout(
            @PathVariable UUID workoutId,
            @RequestBody WorkoutDTO workoutDTO,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutService.verifyOwnership(workoutId, user.getUserId());

        workoutDTO.setWorkoutId(workoutId);
        WorkoutDTO updatedWorkout = workoutService.updateWorkout(workoutId, workoutDTO);

        return ResponseEntity.ok(updatedWorkout);
    }

    @DeleteMapping("/{workoutId}")
    public ResponseEntity<Void> deleteWorkout(
            @PathVariable UUID workoutId,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutService.verifyOwnership(workoutId, user.getUserId());
        workoutService.deleteById(workoutId);
        return ResponseEntity.noContent().build();
    }

}