package com.valchev.athletiq.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutStreakDTO {
    private int currentStreak;
    private int longestStreak;
    private String lastWorkoutDate;
    private List<String> workoutDates;
}