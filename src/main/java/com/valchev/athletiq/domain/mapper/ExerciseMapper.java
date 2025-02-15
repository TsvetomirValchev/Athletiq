package com.valchev.athletiq.domain.mapper;

import com.valchev.athletiq.domain.dto.ExerciseDTO;
import com.valchev.athletiq.domain.entity.Exercise;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExerciseMapper {

    ExerciseMapper INSTANCE = Mappers.getMapper(ExerciseMapper.class);

    ExerciseDTO toDTO(Exercise exercise);

    @Mapping(target = "exerciseId", ignore = true)
    Exercise toEntity(ExerciseDTO exerciseDTO);
}
