package com.valchev.athletiq.controller;

import com.valchev.athletiq.domain.dto.ExerciseDTO;
import com.valchev.athletiq.security.AthletiqUser;
import com.valchev.athletiq.service.ExerciseService;
import com.valchev.athletiq.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class WorkoutExerciseController {

    private final WorkoutService workoutService;
    private final ExerciseService exerciseService;

    @GetMapping
    public ResponseEntity<List<ExerciseDTO>> getWorkoutExercises(
            @PathVariable UUID workoutId,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutService.verifyOwnership(workoutId, user.getUserId());
        List<ExerciseDTO> exercises = exerciseService.getAllExercisesByWorkoutId(workoutId);
        return ResponseEntity.ok(exercises);
    }

    @PostMapping
    public ResponseEntity<ExerciseDTO> addExerciseToWorkout(
            @PathVariable UUID workoutId,
            @RequestBody ExerciseDTO exerciseDTO,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutService.verifyOwnership(workoutId, user.getUserId());
        exerciseDTO.setWorkoutId(workoutId);
        log.info("Adding exercise to workout: {}", exerciseDTO.getWorkoutId());
        log.info("Exercise details: {}", exerciseDTO);
        log.info("Exercise name: {}", exerciseDTO.getName());
        log.info("Exercise id: {}", exerciseDTO.getExerciseId());
        ExerciseDTO exercise = exerciseService.save(exerciseDTO);
        log.info("Exercise added with ID: {}", exercise.getExerciseId());
        return ResponseEntity.ok(exercise);
    }

    @GetMapping("/{exerciseId}")
    public ResponseEntity<ExerciseDTO> getWorkoutExercise(
            @PathVariable UUID workoutId,
            @PathVariable UUID exerciseId,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutService.verifyOwnership(workoutId, user.getUserId());
        ExerciseDTO exercise = exerciseService.findById(exerciseId);
        return ResponseEntity.ok(exercise);
    }

    @PutMapping("/{exerciseId}")
    public ResponseEntity<ExerciseDTO> updateWorkoutExercise(
            @PathVariable UUID workoutId,
            @PathVariable UUID exerciseId,
            @RequestBody ExerciseDTO exerciseDTO,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutService.verifyOwnership(workoutId, user.getUserId());
        exerciseDTO.setExerciseId(exerciseId);
        ExerciseDTO updatedExercise = exerciseService.updateExercise(exerciseId, exerciseDTO);
        return ResponseEntity.ok(updatedExercise);
    }



    @DeleteMapping("/{exerciseId}")
    public ResponseEntity<Void> removeExerciseFromWorkout(
            @PathVariable UUID workoutId,
            @PathVariable UUID exerciseId,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutService.verifyOwnership(workoutId, user.getUserId());
        exerciseService.deleteById(exerciseId);
        return ResponseEntity.noContent().build();
    }
}
