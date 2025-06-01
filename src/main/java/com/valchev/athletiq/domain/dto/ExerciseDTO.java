package com.valchev.athletiq.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ExerciseDTO {

    private UUID exerciseId;
    private UUID workoutId;
    private UUID exerciseTemplateId;
    private String name;
    private String notes;
    private List<ExerciseSetDTO> sets;
    private int orderPosition;

}