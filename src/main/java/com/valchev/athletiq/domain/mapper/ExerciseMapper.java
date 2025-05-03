package com.valchev.athletiq.domain.mapper;

import com.valchev.athletiq.domain.dto.ExerciseDTO;
import com.valchev.athletiq.domain.entity.Exercise;
import com.valchev.athletiq.domain.entity.ExerciseSet;
import com.valchev.athletiq.service.ExerciseSetService;
import org.mapstruct.Context;
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

    @Mapping(source = "exerciseId", target = "exerciseId")
    @Mapping(source = "workoutId", target = "workout.workoutId")
    @Mapping(source = "exerciseTemplateId", target = "exerciseTemplate.exerciseTemplateId")
    @Mapping(source = "exerciseSetIds", target = "sets", qualifiedByName = "mapSetIdsToSets")
    Exercise toEntity(ExerciseDTO exerciseDTO, @Context ExerciseSetService exerciseSetService);

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

    @Named("mapSetIdsToSets")
    default List<ExerciseSet> mapSetIdsToSets(List<UUID> setIds, @Context ExerciseSetService exerciseSetService) {
        if (setIds == null) {
            return null;
        }
        return exerciseSetService.findAllByIds(setIds);
    }

}