package com.valchev.athletiq.controller;


import com.valchev.athletiq.domain.dto.WorkoutStats;
import com.valchev.athletiq.service.WorkoutHistoryService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@Slf4j
public class UserStatisticsController {

    private final WorkoutHistoryService workoutHistoryService;

    @GetMapping("/profile-page-stats")
    public ResponseEntity<WorkoutStats> getUserWorkoutStats(@RequestParam UUID userId) {
        WorkoutStats stats = workoutHistoryService.calculateWorkoutStats(userId);
        return ResponseEntity.ok(stats);
    }

}
