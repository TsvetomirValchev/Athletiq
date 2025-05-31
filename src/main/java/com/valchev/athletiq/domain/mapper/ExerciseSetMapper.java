package com.valchev.athletiq.domain.mapper;

import com.valchev.athletiq.domain.dto.ExerciseSetDTO;
import com.valchev.athletiq.domain.entity.Exercise;
import com.valchev.athletiq.domain.entity.ExerciseSet;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExerciseSetMapper {

    @Mapping(source = "exercise.exerciseId", target = "exerciseId")
    ExerciseSetDTO toDTO(ExerciseSet exerciseSet);

    @Mapping(source = "exerciseId", target = "exercise.exerciseId")
    ExerciseSet toEntity(ExerciseSetDTO exerciseSetDTO);

    List<ExerciseSetDTO> toDTOs(List<ExerciseSet> exerciseSets);

    List<ExerciseSet> toEntities(List<ExerciseSetDTO> exerciseSetDTOs);

    void update(@MappingTarget ExerciseSet entity, ExerciseSet updateEntity);

}