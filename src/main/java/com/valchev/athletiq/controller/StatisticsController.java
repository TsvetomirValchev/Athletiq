package com.valchev.athletiq.controller;


import com.valchev.athletiq.domain.dto.CalendarDayData;
import com.valchev.athletiq.domain.dto.MuscleGroupStats;
import com.valchev.athletiq.domain.dto.WorkoutStats;
import com.valchev.athletiq.domain.dto.WorkoutStreakData;
import com.valchev.athletiq.service.StatisticsService;
import com.valchev.athletiq.service.WorkoutHistoryService;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@Slf4j
public class StatisticsController {

    private final WorkoutHistoryService workoutHistoryService;
    private final StatisticsService statisticsService;

    @GetMapping("/profile-page-stats")
    public ResponseEntity<WorkoutStats> getUserWorkoutStats(@RequestParam UUID userId) {
        WorkoutStats stats = workoutHistoryService.calculateWorkoutStats(userId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/streaks")
    public ResponseEntity<WorkoutStreakData> getWorkoutStreaks(@RequestParam UUID userId) {
        return ResponseEntity.ok(statisticsService.getWorkoutStreaks(userId));
    }

    @GetMapping("/calendar/{year}/{month}")
    public ResponseEntity<List<CalendarDayData>> getCalendarData(
            @PathVariable int year,
            @PathVariable int month,
            @RequestParam UUID userId) {
        return ResponseEntity.ok(statisticsService.getCalendarData(year, month, userId));
    }

    @GetMapping("/muscle-groups")
    public ResponseEntity<List<MuscleGroupStats>> getMuscleGroupDistribution(@RequestParam UUID userId) {
        return ResponseEntity.ok(statisticsService.getMuscleGroupDistribution(userId));
    }

}
