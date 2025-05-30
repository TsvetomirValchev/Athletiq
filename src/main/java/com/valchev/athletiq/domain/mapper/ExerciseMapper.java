package com.valchev.athletiq.domain.mapper;

import com.valchev.athletiq.domain.dto.ExerciseDTO;
import com.valchev.athletiq.domain.dto.ExerciseSetDTO;
import com.valchev.athletiq.domain.entity.Exercise;
import com.valchev.athletiq.domain.entity.ExerciseSet;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ExerciseSetMapper.class})
public interface ExerciseMapper {

    @Mapping(source = "workout.workoutId", target = "workoutId")
    @Mapping(source = "exerciseTemplate.exerciseTemplateId", target = "exerciseTemplateId")
    @Mapping(source = "exerciseTemplate.name", target = "name")
    @Mapping(target = "orderPosition", source = "orderPosition")
    @Mapping(source = "sets", target = "sets")
    @Mapping(source = "sets", target = "totalSets", qualifiedByName = "countSets")
    @Mapping(source = "sets", target = "maxWeight", qualifiedByName = "calculateMaxWeight")
    @Mapping(source = "sets", target = "totalReps", qualifiedByName = "calculateTotalReps")
    ExerciseDTO toDTO(Exercise exercise);

    @Mapping(source = "exerciseId", target = "exerciseId")
    @Mapping(source = "workoutId", target = "workout.workoutId")
    @Mapping(source = "exerciseTemplateId", target = "exerciseTemplate.exerciseTemplateId")
    @Mapping(target = "sets", ignore = true)
    @Mapping(target = "orderPosition", source = "orderPosition")
    Exercise toEntity(ExerciseDTO exerciseDTO);

    @Mapping(target = "sets", ignore = true)
    void update(@MappingTarget Exercise entity, Exercise updateEntity);

    List<ExerciseDTO> toDTOs(List<Exercise> exercises);

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

    @AfterMapping
    default void mapSets(ExerciseDTO dto, @MappingTarget Exercise exercise) {
        if (dto.getSets() != null) {
            List<ExerciseSet> sets = dto.getSets().stream()
                    .map(setDto -> {
                        ExerciseSet set = new ExerciseSet();
                        set.setExerciseSetId(setDto.getExerciseSetId());
                        set.setOrderPosition(setDto.getOrderPosition());
                        set.setReps(setDto.getReps());
                        set.setWeight(setDto.getWeight());
                        set.setRestTimeSeconds(setDto.getRestTimeSeconds());
                        set.setType(setDto.getType());
                        set.setExercise(exercise); // Set the parent reference
                        return set;
                    })
                    .collect(Collectors.toList());
            exercise.setSets(sets);
        }
    }
}