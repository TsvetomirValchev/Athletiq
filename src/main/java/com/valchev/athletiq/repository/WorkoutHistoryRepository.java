package com.valchev.athletiq.repository;

import com.valchev.athletiq.domain.entity.WorkoutHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkoutHistoryRepository extends JpaRepository<WorkoutHistory, UUID> {
    List<WorkoutHistory> findAllByUserId(UUID userId);

    @Query(value = "SELECT COUNT(DISTINCT DATE(w.date)) FROM workout_history w WHERE w.user_id = :userId", nativeQuery = true)
    int countDistinctWorkoutDaysByUserId(@Param("userId") UUID userId);
}