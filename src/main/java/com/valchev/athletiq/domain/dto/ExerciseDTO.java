package com.valchev.athletiq.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExerciseDTO {
    private String name;
    private double weight;
    private int sets;
}
