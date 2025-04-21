package com.valchev.athletiq.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class ExerciseDTO {

    private UUID exerciseId;
    private UUID workoutId;
    private UUID exerciseTemplateId;
    private String name;
    private String description;
    private String notes;
    private List<UUID> exerciseSetIds;
    private int totalSets;
    private double maxWeight;
    private int totalReps;

}