package com.valchev.athletiq.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hibernate.annotations.CascadeType.ALL;

@Entity(name = "exercise")
@Data
@Slf4j
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID exerciseId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Workout workout;

    @ManyToOne(fetch = FetchType.EAGER)
    private ExerciseTemplate exerciseTemplate;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cascade(ALL)
    private List<ExerciseSet> sets = new ArrayList<>();

    private String notes;

    public void addSet(ExerciseSet set) {
        sets.add(set);
        set.setExercise(this);
        set.setOrderPosition(sets.size());
    }

    public void removeSet(ExerciseSet set) {
        sets.remove(set);
    }


}