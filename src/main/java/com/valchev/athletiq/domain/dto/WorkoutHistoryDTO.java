package com.valchev.athletiq.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutHistoryDTO {

    private UUID workoutHistoryId;

    private String name;

    private UUID userId;

    @NotNull
    private LocalDate date;

    @NotNull
    private Duration duration;

    @Builder.Default
    private List<ExerciseHistoryDTO> exerciseHistories = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Timestamp createdAt;
}