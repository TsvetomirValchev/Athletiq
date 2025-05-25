package com.valchev.athletiq.controller;

import com.valchev.athletiq.domain.dto.ExerciseHistoryDTO;
import com.valchev.athletiq.domain.dto.WorkoutHistoryDTO;
import com.valchev.athletiq.domain.entity.ExerciseHistory;
import com.valchev.athletiq.domain.entity.WorkoutHistory;
import com.valchev.athletiq.domain.exception.ResourceNotFoundException;
import com.valchev.athletiq.domain.mapper.ExerciseHistoryMapper;
import com.valchev.athletiq.domain.mapper.WorkoutHistoryMapper;
import com.valchev.athletiq.service.WorkoutHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/workouts/history")
@RequiredArgsConstructor
@Slf4j
public class WorkoutHistoryController {

    private final WorkoutHistoryService workoutHistoryService;
    private final WorkoutHistoryMapper workoutHistoryMapper;

    @GetMapping("/{workoutHistoryId}")
    public ResponseEntity<WorkoutHistoryDTO> getWorkoutHistory(@PathVariable UUID workoutHistoryId) {
        log.info("Fetching workout history with ID: {}", workoutHistoryId);
        WorkoutHistoryDTO workoutHistoryDTO = workoutHistoryService.findById(workoutHistoryId);
        return ResponseEntity.ok(workoutHistoryDTO);

    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WorkoutHistoryDTO>> getAllWorkoutHistoryForUser(@PathVariable UUID userId) {
        log.info("Fetching workout histories for user: {}", userId);
        List<WorkoutHistoryDTO> workoutHistory = workoutHistoryService.findAllByUserId(userId);
        log.info("Found {} workout histories for user {}", workoutHistory.size(), userId);
        return ResponseEntity.ok(workoutHistory);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<WorkoutHistoryDTO> createWorkoutHistory(@Valid @RequestBody WorkoutHistoryDTO workoutHistoryDTO) {
        log.info("Creating workout history: {}", workoutHistoryDTO);
        WorkoutHistory savedHistory = workoutHistoryService.saveWorkoutToHistory(workoutHistoryDTO);
        WorkoutHistoryDTO response = workoutHistoryMapper.toDTO(savedHistory);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}