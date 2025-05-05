package com.valchev.athletiq.controller;

import com.valchev.athletiq.domain.dto.ExerciseDTO;
import com.valchev.athletiq.domain.dto.ExerciseSetDTO;
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
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("/{workoutId}/exercises")
    public ResponseEntity<List<ExerciseDTO>> getWorkoutExercises(
            @PathVariable UUID workoutId,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutService.verifyOwnership(workoutId, user.getUserId());
        List<ExerciseDTO> exercises = workoutService.getExercisesByWorkoutId(workoutId);
        return ResponseEntity.ok(exercises);
    }

    @PostMapping("/{workoutId}/exercises")
    public ResponseEntity<WorkoutDTO> addExerciseToWorkout(
            @PathVariable UUID workoutId,
            @RequestBody ExerciseDTO exerciseDTO,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutService.verifyOwnership(workoutId, user.getUserId());

        WorkoutDTO updatedWorkout = workoutService.addExerciseWithSets(workoutId, exerciseDTO);
        return ResponseEntity.ok(updatedWorkout);
    }

    @PostMapping("/{workoutId}/batch-exercises")
    public ResponseEntity<WorkoutDTO> addMultipleExercisesToWorkout(
            @PathVariable UUID workoutId,
            @RequestBody List<ExerciseDTO> exerciseDTOs,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutService.verifyOwnership(workoutId, user.getUserId());
        WorkoutDTO updatedWorkout = workoutService.addBatchExercisesWithSets(workoutId, exerciseDTOs);
        return ResponseEntity.ok(updatedWorkout);
    }

    @GetMapping("/{workoutId}/exercises/{exerciseId}")
    public ResponseEntity<ExerciseDTO> getWorkoutExercise(
            @PathVariable UUID workoutId,
            @PathVariable UUID exerciseId,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutService.verifyOwnership(workoutId, user.getUserId());
        ExerciseDTO exercise = workoutService.getWorkoutExerciseById(workoutId, exerciseId);
        return ResponseEntity.ok(exercise);
    }

    @PutMapping("/{workoutId}/exercises/{exerciseId}")
    public ResponseEntity<WorkoutDTO> updateWorkoutExercise(
            @PathVariable UUID workoutId,
            @PathVariable UUID exerciseId,
            @RequestBody ExerciseDTO exerciseDTO,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutService.verifyOwnership(workoutId, user.getUserId());
        exerciseDTO.setExerciseId(exerciseId);
        WorkoutDTO updatedWorkout = workoutService.updateWorkoutExercise(workoutId, exerciseDTO);
        return ResponseEntity.ok(updatedWorkout);
    }

    @DeleteMapping("/{workoutId}/exercises/{exerciseId}")
    public ResponseEntity<WorkoutDTO> removeExerciseFromWorkout(
            @PathVariable UUID workoutId,
            @PathVariable UUID exerciseId,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutService.verifyOwnership(workoutId, user.getUserId());
        WorkoutDTO updatedWorkout = workoutService.removeExerciseFromWorkout(workoutId, exerciseId);
        return ResponseEntity.ok(updatedWorkout);
    }

    @GetMapping("/{workoutId}/exercises/{exerciseId}/sets")
    public ResponseEntity<List<ExerciseSetDTO>> getExerciseSets(
            @PathVariable UUID workoutId,
            @PathVariable UUID exerciseId,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutService.verifyOwnership(workoutId, user.getUserId());
        List<ExerciseSetDTO> sets = workoutService.getExerciseSets(workoutId, exerciseId);
        return ResponseEntity.ok(sets);
    }

    @PostMapping("/{workoutId}/exercises/{exerciseId}/sets")
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

    @DeleteMapping("/{workoutId}/exercises/{exerciseId}/sets/{orderPosition}")
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