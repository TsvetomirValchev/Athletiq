package com.valchev.athletiq.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Entity(name = "exercise")
@Data
@Slf4j
public class Exercise {

    @Id
    private UUID exerciseId;
    private String name;
    private double weight;
    private int sets;

    @ManyToOne
    private Workout workout;

}
