package com.valchev.athletiq.domain.dto;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ExerciseDTO {

    private String name;
    private double weight;
    private int sets;
    private UUID workoutId;
    private int reps;
    private Map<Double, Integer> highestVolume;
}
