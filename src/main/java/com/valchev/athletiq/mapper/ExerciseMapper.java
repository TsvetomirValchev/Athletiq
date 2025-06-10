package com.valchev.athletiq.mapper;

import com.valchev.athletiq.domain.dto.ExerciseDTO;
import com.valchev.athletiq.domain.entity.Exercise;
import com.valchev.athletiq.domain.entity.ExerciseSet;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {SetMapper.class})
public interface ExerciseMapper {

    @Mapping(source = "workout.workoutId", target = "workoutId")
    @Mapping(source = "exerciseTemplate.exerciseTemplateId", target = "exerciseTemplateId")
    @Mapping(source = "orderPosition", target = "orderPosition")
    @Mapping(source = "sets", target = "sets")
    @Mapping(source = "exerciseId", target = "exerciseId")
    @Mapping(source = "exerciseTemplate.name", target = "name")
    ExerciseDTO toDTO(Exercise exercise);

    @Mapping(source = "exerciseId", target = "exerciseId")
    @Mapping(source = "workoutId", target = "workout.workoutId")
    @Mapping(target = "sets", ignore = true)
    @Mapping(source = "orderPosition", target = "orderPosition")
    @Mapping(target = "exerciseTemplate", ignore = true)
    Exercise toEntity(ExerciseDTO exerciseDTO);

    @Mapping(target = "sets", ignore = true)
    void update(@MappingTarget Exercise entity, ExerciseDTO dto);

    List<ExerciseDTO> toDTOs(List<Exercise> exercises);

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