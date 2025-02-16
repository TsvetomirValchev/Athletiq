package com.valchev.athletiq.domain.mapper;

import com.valchev.athletiq.domain.dto.ActiveWorkoutDTO;
import com.valchev.athletiq.domain.dto.WorkoutDTO;
import com.valchev.athletiq.domain.entity.ActiveWorkout;
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

    @Mapping(source = "exercises", target = "exercises")
    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "endTime", target = "endTime")
    ActiveWorkoutDTO toDTO(ActiveWorkout activeWorkout);

    @Mapping(target = "workoutId", ignore = true)
    @Mapping(source = "exercises", target = "exercises", ignore = true)
    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "endTime", target = "endTime")
    ActiveWorkout toEntity(ActiveWorkoutDTO activeWorkoutDTO);
}
