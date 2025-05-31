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

    Optional<ExerciseTemplate> findByNameIgnoreCase(String name);


    @Query("SELECT et.targetMuscleGroups FROM ExerciseTemplate et WHERE et.name = :exerciseName")
    List<String> findMuscleGroupsByExerciseName(@Param("exerciseName") String exerciseName);
}
