package com.valchev.athletiq.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "exercise_set")
@Data
public class ExerciseSet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID exerciseSetId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Exercise exercise;

    @Column(nullable = false)
    private Integer orderPosition;

    @Column(nullable = false)
    private Integer reps;

    @Column(nullable = false)
    private Double weight;

    @Column
    private Integer restTimeSeconds;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SetType type;

}