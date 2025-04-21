package com.valchev.athletiq.domain.mapper;

import com.valchev.athletiq.domain.dto.ExerciseSetDTO;
import com.valchev.athletiq.domain.entity.ExerciseSet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExerciseSetMapper {

    @Mapping(source = "exercise.exerciseId", target = "exerciseId")
    @Mapping(source = "type", target = "type")
    ExerciseSetDTO toDTO(ExerciseSet exerciseSet);

    @Mapping(target = "exerciseSetId", ignore = true)
    @Mapping(target = "exercise", ignore = true)
    @Mapping(source = "type", target = "type")
    ExerciseSet toEntity(ExerciseSetDTO exerciseSetDTO);

    List<ExerciseSetDTO> toDTOs(List<ExerciseSet> exerciseSets);
}