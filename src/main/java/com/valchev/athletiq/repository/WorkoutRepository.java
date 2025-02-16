package com.valchev.athletiq.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.valchev.athletiq.domain.entity.Workout;

public interface WorkoutRepository extends JpaRepository<Workout, UUID> {
}
