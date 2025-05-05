package com.valchev.athletiq.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
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
