package com.valchev.athletiq.repository;

import com.valchev.athletiq.domain.entity.ExerciseTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExerciseTemplateRepository extends JpaRepository<ExerciseTemplate, UUID> {

    Optional<ExerciseTemplate> findByName(String name);

    @Query("SELECT e FROM ExerciseTemplate e JOIN e.targetMuscleGroups m WHERE m = :muscleGroup")
    List<ExerciseTemplate> findByTargetMuscleGroup(@Param("muscleGroup") String muscleGroup);
}
