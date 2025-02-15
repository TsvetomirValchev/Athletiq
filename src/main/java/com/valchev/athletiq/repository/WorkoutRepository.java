package com.valchev.athletiq.repository;

import com.valchev.athletiq.domain.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface WorkoutRepository extends JpaRepository<Workout, UUID> {
}
