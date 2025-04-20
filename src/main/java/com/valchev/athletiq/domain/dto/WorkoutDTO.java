package com.valchev.athletiq.domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutDTO {

    private UUID workoutId;

    private String name;

    private UUID userId;

    private List<UUID> exerciseIds;

}
