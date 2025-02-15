package com.valchev.athletiq.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WorkoutDTO {
    private String name;
    private List<ExerciseDTO> exercises;
}
