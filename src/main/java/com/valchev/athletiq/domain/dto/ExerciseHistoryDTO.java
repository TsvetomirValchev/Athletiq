package com.valchev.athletiq.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseHistoryDTO {

    private UUID exerciseHistoryId;
    private String exerciseName;
    private int orderPosition;
    private String notes;

    @Builder.Default
    private List<ExerciseSetHistoryDTO> exerciseSetHistories = new ArrayList<>();

}