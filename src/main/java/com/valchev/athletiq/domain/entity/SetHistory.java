package com.valchev.athletiq.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "set_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID exerciseSetHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_history_id")
    private ExerciseHistory exerciseHistory;

    @Column(nullable = false)
    private int orderPosition;

    @Column
    private Integer reps;

    @Column
    private Double weight;

    @Column
    private boolean completed;

    @Column
    private SetType type;
}