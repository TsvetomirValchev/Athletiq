package com.valchev.athletiq.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "workout_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID workoutHistoryId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private LocalDate date;

    @Column
    private Duration duration;

    @OneToMany(mappedBy = "workoutHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseHistory> exerciseHistories = new ArrayList<>();

}