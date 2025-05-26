package com.valchev.athletiq.controller;

import com.valchev.athletiq.domain.dto.ExerciseDTO;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/workouts/{workoutId}/exercises")
@RequiredArgsConstructor
public class WorkoutExerciseController {

    private final WorkoutService workoutService;

    @GetMapping
    public ResponseEntity<List<ExerciseDTO>> getWorkoutExercises(
            @PathVariable UUID workoutId,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutService.verifyOwnership(workoutId, user.getUserId());
        List<ExerciseDTO> exercises = workoutService.getExercisesByWorkoutId(workoutId);
        return ResponseEntity.ok(exercises);
    }

    @PostMapping
    public ResponseEntity<WorkoutDTO> addExerciseToWorkout(
            @PathVariable UUID workoutId,
            @RequestBody ExerciseDTO exerciseDTO,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutService.verifyOwnership(workoutId, user.getUserId());

        WorkoutDTO updatedWorkout = workoutService.addExerciseWithSets(workoutId, exerciseDTO);
        return ResponseEntity.ok(updatedWorkout);
    }

    @PostMapping("/batch-exercises")
    public ResponseEntity<WorkoutDTO> addMultipleExercisesToWorkout(
            @PathVariable UUID workoutId,
            @RequestBody List<ExerciseDTO> exerciseDTOs,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutService.verifyOwnership(workoutId, user.getUserId());
        WorkoutDTO updatedWorkout = workoutService.addBatchExercisesWithSets(workoutId, exerciseDTOs);
        return ResponseEntity.ok(updatedWorkout);
    }

    @GetMapping("/{exerciseId}")
    public ResponseEntity<ExerciseDTO> getWorkoutExercise(
            @PathVariable UUID workoutId,
            @PathVariable UUID exerciseId,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutService.verifyOwnership(workoutId, user.getUserId());
        ExerciseDTO exercise = workoutService.getWorkoutExerciseById(workoutId, exerciseId);
        return ResponseEntity.ok(exercise);
    }

    @PutMapping("/{exerciseId}")
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



    @DeleteMapping("/{exerciseId}")
    public ResponseEntity<WorkoutDTO> removeExerciseFromWorkout(
            @PathVariable UUID workoutId,
            @PathVariable UUID exerciseId,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutService.verifyOwnership(workoutId, user.getUserId());
        WorkoutDTO updatedWorkout = workoutService.removeExerciseFromWorkout(workoutId, exerciseId);
        return ResponseEntity.ok(updatedWorkout);
    }
}
