package com.valchev.athletiq.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ExerciseTemplateDTO {

    private UUID exerciseTemplateId;
    private String name;
    private String description;
    private List<String> targetMuscleGroups;
    private String imageUrl;
    private int orderPosition;

}