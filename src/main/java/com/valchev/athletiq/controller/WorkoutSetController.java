package com.valchev.athletiq.controller;

import com.valchev.athletiq.domain.dto.ExerciseSetDTO;
import com.valchev.athletiq.domain.dto.WorkoutDTO;
import com.valchev.athletiq.security.AthletiqUser;
import com.valchev.athletiq.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RequestMapping("/workouts/{workoutId}/exercises/{exerciseId}/sets")
@RestController
@RequiredArgsConstructor
public class WorkoutSetController {

    private final WorkoutService workoutService;

    @GetMapping
    public ResponseEntity<List<ExerciseSetDTO>> getExerciseSets(
            @PathVariable UUID workoutId,
            @PathVariable UUID exerciseId,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutService.verifyOwnership(workoutId, user.getUserId());
        List<ExerciseSetDTO> sets = workoutService.getExerciseSets(workoutId, exerciseId);
        return ResponseEntity.ok(sets);
    }

    @PostMapping
    public ResponseEntity<WorkoutDTO> addSetToExercise(
            @PathVariable UUID workoutId,
            @PathVariable UUID exerciseId,
            @RequestBody ExerciseSetDTO setDTO,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutService.verifyOwnership(workoutId, user.getUserId());
        WorkoutDTO updatedWorkout = workoutService.addSetToExercise(workoutId, exerciseId, setDTO);
        return ResponseEntity.ok(updatedWorkout);
    }

    @DeleteMapping("/{orderPosition}")
    public ResponseEntity<WorkoutDTO> removeSetFromExercise(
            @PathVariable UUID workoutId,
            @PathVariable UUID exerciseId,
            @PathVariable Integer orderPosition,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutService.verifyOwnership(workoutId, user.getUserId());
        WorkoutDTO updatedWorkout = workoutService.removeSetFromExercise(workoutId, exerciseId, orderPosition);
        return ResponseEntity.ok(updatedWorkout);
    }
}
