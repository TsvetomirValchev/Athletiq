package com.valchev.athletiq.repository;

import com.valchev.athletiq.domain.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {
}
