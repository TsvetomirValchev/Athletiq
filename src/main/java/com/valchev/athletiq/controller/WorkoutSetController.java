package com.valchev.athletiq.controller;

import com.valchev.athletiq.domain.dto.ExerciseSetDTO;
import com.valchev.athletiq.domain.dto.WorkoutDTO;
import com.valchev.athletiq.security.AthletiqUser;
import com.valchev.athletiq.service.ExerciseSetService;
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


@RequestMapping("/workouts/{workoutId}/exercises/{exerciseId}/sets")
@RestController
@RequiredArgsConstructor
@Slf4j
public class WorkoutSetController {

    private final ExerciseSetService exerciseSetService;
    private final WorkoutService workoutService;

    @GetMapping
    public ResponseEntity<List<ExerciseSetDTO>> getExerciseSets(
            @PathVariable UUID workoutId,
            @PathVariable UUID exerciseId,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutService.verifyOwnership(workoutId, user.getUserId());
        List<ExerciseSetDTO> sets = exerciseSetService.getExerciseSetsByExerciseId(exerciseId);
        return ResponseEntity.ok(sets);
    }

    @PostMapping
    public ResponseEntity<ExerciseSetDTO> addSetToExercise(
            @PathVariable UUID workoutId,
            @PathVariable UUID exerciseId,
            @RequestBody ExerciseSetDTO setDTO,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutService.verifyOwnership(workoutId, user.getUserId());
        setDTO.setExerciseId(exerciseId);
        log.info("Received set: {} " , setDTO);
        log.info("For exercise ID: {} ", exerciseId);
        return ResponseEntity.ok(exerciseSetService.save(setDTO));
    }

    @DeleteMapping("/{orderPosition}")
    public ResponseEntity<?> removeSetFromExercise(
            @PathVariable UUID workoutId,
            @PathVariable UUID exerciseId,
            @PathVariable Integer orderPosition,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutService.verifyOwnership(workoutId, user.getUserId());
        exerciseSetService.deleteByOrderPosition(orderPosition, exerciseId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{exerciseSetId}")
    public ResponseEntity<ExerciseSetDTO> updateExerciseSet(
            @PathVariable UUID workoutId,
            @PathVariable UUID exerciseId,
            @PathVariable UUID exerciseSetId,
            @RequestBody ExerciseSetDTO setDTO,
            Authentication authentication) {
        AthletiqUser user = (AthletiqUser) authentication.getDetails();
        workoutService.verifyOwnership(workoutId, user.getUserId());
        log.info("Updating set with ID: {}", exerciseSetId);
        log.info("For in update set exercise ID: {}", exerciseId);
        log.info("setDto {} ", setDTO);
        setDTO.setExerciseId(exerciseId);
        exerciseSetService.updateExerciseSet(exerciseSetId, setDTO);
        return ResponseEntity.noContent().build();
    }
}
