package com.valchev.athletiq.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkoutStatsDTO {
    private int totalWorkouts;
    private int uniqueDays;
    private int hoursActive;
}