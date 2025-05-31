package com.valchev.athletiq.repository;

import com.valchev.athletiq.domain.entity.ExerciseSetHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SetHistoryRepository extends JpaRepository<ExerciseSetHistory, UUID> {
}
