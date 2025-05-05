package com.valchev.athletiq.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseTemplateDTO {
    private UUID exerciseTemplateId;
    private String name;
    private String description;
    private List<String> targetMuscleGroups;
    private String imageUrl;
}