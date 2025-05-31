package com.valchev.athletiq.domain.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "exercise_template")
@Data
public class ExerciseTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID exerciseTemplateId;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @ElementCollection
    @CollectionTable(
            name = "exercise_muscle_groups",
            joinColumns = @JoinColumn(name = "exercise_id")
    )
    @Column(name = "muscle_group")
    private List<String> targetMuscleGroups;

    @Column(name = "image_url")
    private String imageUrl;

}