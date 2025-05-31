package com.valchev.athletiq.repository;

import com.valchev.athletiq.domain.entity.ExerciseSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExerciseSetRepository extends JpaRepository<ExerciseSet, UUID> {
    List<ExerciseSet> findAllByExercise_ExerciseId(UUID exerciseId);
    Optional<ExerciseSet> findByOrderPositionAndExercise_ExerciseId(Integer orderPosition, UUID exerciseId);
}
