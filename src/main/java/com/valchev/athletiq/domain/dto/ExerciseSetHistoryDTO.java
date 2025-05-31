package com.valchev.athletiq.domain.dto;

import com.valchev.athletiq.domain.entity.SetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseSetHistoryDTO {

    private UUID exerciseSetHistoryId;
    private int orderPosition;
    private int reps;
    private double weight;
    private boolean completed;
    private SetType type;

}