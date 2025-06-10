package com.valchev.athletiq.mapper;

import com.valchev.athletiq.domain.dto.SetDTO;
import com.valchev.athletiq.domain.entity.ExerciseSet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SetMapper {

    @Mapping(source = "exercise.exerciseId", target = "exerciseId")
    SetDTO toDTO(ExerciseSet exerciseSet);

    @Mapping(source = "exerciseId", target = "exercise.exerciseId")
    ExerciseSet toEntity(SetDTO setDTO);

    List<SetDTO> toDTOs(List<ExerciseSet> exerciseSets);

    List<ExerciseSet> toEntities(List<SetDTO> setDTOS);

    void update(@MappingTarget ExerciseSet entity, ExerciseSet updateEntity);

}