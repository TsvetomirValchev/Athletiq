package com.valchev.athletiq.domain.mapper;

import com.valchev.athletiq.domain.dto.ExerciseDTO;
import com.valchev.athletiq.domain.entity.Exercise;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


import java.util.List;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {

    ExerciseMapper INSTANCE = Mappers.getMapper(ExerciseMapper.class);

    @Mapping(source = "workout.workoutId", target = "workoutId")
    ExerciseDTO toDTO(Exercise exercise);

    @Mapping(target = "exerciseId", ignore = true)
    @Mapping(target = "workout", ignore = true)
    Exercise toEntity(ExerciseDTO exerciseDTO);

    List<ExerciseDTO> toDTOs(List<Exercise> exercises);
}
