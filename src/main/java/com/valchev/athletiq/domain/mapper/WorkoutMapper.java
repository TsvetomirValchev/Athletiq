package com.valchev.athletiq.domain.mapper;

import com.valchev.athletiq.domain.dto.WorkoutDTO;
import com.valchev.athletiq.domain.entity.Workout;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WorkoutMapper {

    WorkoutMapper INSTANCE = Mappers.getMapper(WorkoutMapper.class);

    @Mapping(source = "exercises", target = "exercises")
    WorkoutDTO toDTO(Workout workout);

    @Mapping(target = "workoutId", ignore = true)
    @Mapping(source = "exercises", target = "exercises", ignore = true)
    Workout toEntity(WorkoutDTO workoutDTO);
}
