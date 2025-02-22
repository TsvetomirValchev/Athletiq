package com.valchev.athletiq.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class WorkoutDTO {
    private String name;
    private UUID userId;
    private List<UUID> exerciseIds;
}
