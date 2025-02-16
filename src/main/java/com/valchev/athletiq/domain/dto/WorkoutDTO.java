package com.valchev.athletiq.domain.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkoutDTO {

    private String name;
    private List<ExerciseDTO> exercises;

}
