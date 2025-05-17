package com.valchev.athletiq.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "exercise_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID exerciseHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_history_id")
    private WorkoutHistory workoutHistory;

    @Column
    private String exerciseName;

    @Column(nullable = false)
    private int orderPosition;

    @Column
    private String notes;

    @OneToMany(mappedBy = "exerciseHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseSetHistory> exerciseSetHistories = new ArrayList<>();

}