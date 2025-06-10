package com.valchev.athletiq.domain.dto;

import com.valchev.athletiq.domain.entity.SetType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class SetDTO {

    private UUID exerciseSetId;
    private UUID exerciseId;
    private Integer orderPosition;
    private Integer reps;
    private Double weight;
    private Integer restTimeSeconds;
    private SetType type;
    private Boolean completed;
    private LocalDateTime lastModified;

}