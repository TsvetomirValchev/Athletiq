package com.valchev.athletiq.domain.entity;

import java.util.Map;
import java.util.UUID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Entity(name = "exercise")
@Data
@Slf4j
public class Exercise {

    @Id
    private UUID exerciseId;
    private String name;
    private double weight;
    private int sets;
    private int reps;

    @ManyToOne
    private Workout workout;

    @ElementCollection
    @CollectionTable(name = "exercise_highest_volume", joinColumns = @JoinColumn(name = "exercise_id"))
    @MapKeyColumn(name = "weight")
    @Column(name = "reps")
    private Map<Double, Integer> highestVolume;

}
