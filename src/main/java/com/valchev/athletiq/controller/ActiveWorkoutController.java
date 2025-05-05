package com.valchev.athletiq.controller;

import com.valchev.athletiq.domain.dto.ActiveWorkoutDTO;
import com.valchev.athletiq.domain.dto.ExerciseDTO;
import com.valchev.athletiq.domain.dto.ExerciseSetDTO;
import com.valchev.athletiq.domain.dto.WorkoutDTO;
import com.valchev.athletiq.security.AthletiqUser;
import com.valchev.athletiq.service.ActiveWorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/active-workouts")
@RequiredArgsConstructor
public class ActiveWorkoutController {

    private final ActiveWorkoutService activeWorkoutService;

    @PostMapping
    public ResponseEntity<ActiveWorkoutDTO> startWorkout(
            @RequestBody ActiveWorkoutDTO workoutDTO,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutDTO.setUserId(user.getUserId());
        return ResponseEntity.ok(activeWorkoutService.startWorkout(workoutDTO));
    }

    @GetMapping
    public ResponseEntity<List<ActiveWorkoutDTO>> getActiveWorkouts(
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        return ResponseEntity.ok(activeWorkoutService.findActiveWorkoutsByUserId(user.getUserId()));
    }

    @PostMapping("/{workoutId}/finish")
    public ResponseEntity<ActiveWorkoutDTO> finishWorkout(
            @PathVariable UUID workoutId,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        activeWorkoutService.verifyOwnership(workoutId, user.getUserId());
        return ResponseEntity.ok(activeWorkoutService.finishWorkout(workoutId));
    }

    @GetMapping("/{workoutId}/exercises")
    public ResponseEntity<List<ExerciseDTO>> getWorkoutExercises(
            @PathVariable UUID workoutId,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        activeWorkoutService.verifyOwnership(workoutId, user.getUserId());
        List<ExerciseDTO> exercises = activeWorkoutService.getExercisesByWorkoutId(workoutId);
        return ResponseEntity.ok(exercises);
    }

    @PostMapping("/{workoutId}/exercises")
    public ResponseEntity<ActiveWorkoutDTO> addExerciseToWorkout(
            @PathVariable UUID workoutId,
            @RequestBody ExerciseDTO exerciseDTO,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        activeWorkoutService.verifyOwnership(workoutId, user.getUserId());
        ActiveWorkoutDTO updatedWorkout = activeWorkoutService.addExerciseToWorkout(workoutId, exerciseDTO);
        return ResponseEntity.ok(updatedWorkout);
    }

    @GetMapping("/{workoutId}/exercises/{exerciseId}")
    public ResponseEntity<ExerciseDTO> getWorkoutExercise(
            @PathVariable UUID workoutId,
            @PathVariable UUID exerciseId,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        activeWorkoutService.verifyOwnership(workoutId, user.getUserId());
        ExerciseDTO exercise = activeWorkoutService.getWorkoutExerciseById(workoutId, exerciseId);
        return ResponseEntity.ok(exercise);
    }

    @PutMapping("/{workoutId}/exercises/{exerciseId}")
    public ResponseEntity<ActiveWorkoutDTO> updateWorkoutExercise(
            @PathVariable UUID workoutId,
            @PathVariable UUID exerciseId,
            @RequestBody ExerciseDTO exerciseDTO,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        activeWorkoutService.verifyOwnership(workoutId, user.getUserId());
        exerciseDTO.setExerciseId(exerciseId);
        ActiveWorkoutDTO updatedWorkout = activeWorkoutService.updateWorkoutExercise(workoutId, exerciseDTO);
        return ResponseEntity.ok(updatedWorkout);
    }

    @DeleteMapping("/{workoutId}/exercises/{exerciseId}")
    public ResponseEntity<ActiveWorkoutDTO> removeExerciseFromWorkout(
            @PathVariable UUID workoutId,
            @PathVariable UUID exerciseId,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        activeWorkoutService.verifyOwnership(workoutId, user.getUserId());
        ActiveWorkoutDTO updatedWorkout = activeWorkoutService.removeExerciseFromWorkout(workoutId, exerciseId);
        return ResponseEntity.ok(updatedWorkout);
    }

    @GetMapping("/{workoutId}/exercises/{exerciseId}/sets")
    public ResponseEntity<List<ExerciseSetDTO>> getExerciseSets(
            @PathVariable UUID workoutId,
            @PathVariable UUID exerciseId,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        activeWorkoutService.verifyOwnership(workoutId, user.getUserId());
        List<ExerciseSetDTO> sets = activeWorkoutService.getExerciseSets(workoutId, exerciseId);
        return ResponseEntity.ok(sets);
    }

    @PostMapping("/{workoutId}/exercises/{exerciseId}/sets")
    public ResponseEntity<ActiveWorkoutDTO> addSetToExercise(
            @PathVariable UUID workoutId,
            @PathVariable UUID exerciseId,
            @RequestBody ExerciseSetDTO setDTO,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        activeWorkoutService.verifyOwnership(workoutId, user.getUserId());
        ActiveWorkoutDTO updatedWorkout = activeWorkoutService.addSetToExercise(workoutId, exerciseId, setDTO);
        return ResponseEntity.ok(updatedWorkout);
    }

    @PutMapping("/{workoutId}/exercises/{exerciseId}/sets/{setId}")
    public ResponseEntity<ActiveWorkoutDTO> completeSet(
            @PathVariable UUID workoutId,
            @PathVariable UUID exerciseId,
            @PathVariable UUID setId,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        activeWorkoutService.verifyOwnership(workoutId, user.getUserId());
        ActiveWorkoutDTO updatedWorkout = activeWorkoutService.completeSet(workoutId, exerciseId, setId);
        return ResponseEntity.ok(updatedWorkout);
    }

    @DeleteMapping("/{workoutId}/exercises/{exerciseId}/sets/{orderPosition}")
    public ResponseEntity<ActiveWorkoutDTO> removeSetFromExercise(
            @PathVariable UUID workoutId,
            @PathVariable UUID exerciseId,
            @PathVariable Integer orderPosition,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        activeWorkoutService.verifyOwnership(workoutId, user.getUserId());
        ActiveWorkoutDTO updatedWorkout = activeWorkoutService.removeSetFromExercise(workoutId, exerciseId, orderPosition);
        return ResponseEntity.ok(updatedWorkout);
    }
}