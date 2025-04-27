package com.valchev.athletiq.domain.mapper;

import com.valchev.athletiq.domain.dto.ExerciseDTO;
import com.valchev.athletiq.domain.entity.Exercise;
import com.valchev.athletiq.domain.entity.ExerciseSet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {

    @Mapping(source = "workout.workoutId", target = "workoutId")
    @Mapping(source = "exerciseTemplate.exerciseTemplateId", target = "exerciseTemplateId")
    @Mapping(source = "exerciseTemplate.name", target = "name")
    @Mapping(source = "sets", target = "exerciseSetIds", qualifiedByName = "mapSetIds")
    @Mapping(source = "sets", target = "totalSets", qualifiedByName = "countSets")
    @Mapping(source = "sets", target = "maxWeight", qualifiedByName = "calculateMaxWeight")
    @Mapping(source = "sets", target = "totalReps", qualifiedByName = "calculateTotalReps")
    ExerciseDTO toDTO(Exercise exercise);

    @Mapping(target = "exerciseId", ignore = true)
    @Mapping(target = "workout", ignore = true)
    @Mapping(target = "exerciseTemplate", ignore = true)
    @Mapping(target = "sets", ignore = true)
    Exercise toEntity(ExerciseDTO exerciseDTO);

    List<ExerciseDTO> toDTOs(List<Exercise> exercises);

    @Named("mapSetIds")
    default List<UUID> mapSetIds(List<ExerciseSet> sets) {
        return sets != null ? sets.stream()
                .map(ExerciseSet::getExerciseSetId)
                .toList() : null;
    }

    @Named("countSets")
    default int countSets(List<ExerciseSet> sets) {
        return sets != null ? sets.size() : 0;
    }

    @Named("calculateMaxWeight")
    default double calculateMaxWeight(List<ExerciseSet> sets) {
        if (sets == null || sets.isEmpty()) return 0;
        return sets.stream()
                .mapToDouble(ExerciseSet::getWeight)
                .max()
                .orElse(0);
    }

    @Named("calculateTotalReps")
    default int calculateTotalReps(List<ExerciseSet> sets) {
        if (sets == null || sets.isEmpty()) return 0;
        return sets.stream()
                .mapToInt(ExerciseSet::getReps)
                .sum();
    }
}