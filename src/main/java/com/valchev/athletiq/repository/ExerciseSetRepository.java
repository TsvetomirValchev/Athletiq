package com.valchev.athletiq.repository;

import com.valchev.athletiq.domain.entity.ExerciseSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExerciseSetRepository extends JpaRepository<ExerciseSet, UUID> {
}
