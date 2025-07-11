package com.valchev.athletiq.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MuscleGroupDTO {
    private String muscleGroup;
    private int workoutCount;
}