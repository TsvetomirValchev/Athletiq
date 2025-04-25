package com.valchev.athletiq.domain.mapper;

import com.valchev.athletiq.domain.dto.ActiveWorkoutDTO;
import com.valchev.athletiq.domain.entity.ActiveWorkout;
import com.valchev.athletiq.domain.entity.Exercise;
import com.valchev.athletiq.service.ExerciseService;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.SubclassMapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ActiveWorkoutMapper {

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "exercises", target = "exerciseIds", qualifiedByName = "mapExerciseIds")
    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "endTime", target = "endTime")
    @SubclassMapping(source = ActiveWorkout.class, target = ActiveWorkoutDTO.class)
    ActiveWorkoutDTO toDTO(ActiveWorkout workout);

    @Mapping(source = "userId", target = "user.userId")
    @Mapping(source = "exerciseIds", target = "exercises", qualifiedByName = "mapExerciseIdsToExercises")
    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "endTime", target = "endTime")
    @SubclassMapping(source = ActiveWorkoutDTO.class, target = ActiveWorkout.class)
    ActiveWorkout toEntity(ActiveWorkoutDTO dto, @Context ExerciseService exerciseService);

    @Named("mapExerciseIds")
    default List<UUID> mapExerciseIds(List<Exercise> exercises) {
        return exercises != null
                ? exercises.stream().map(Exercise::getExerciseId).toList()
                : null;
    }

    @Named("mapExerciseIdsToExercises")
    default List<Exercise> mapExerciseIdsToExercises(List<UUID> exerciseIds, @Context ExerciseService exerciseService) {
        return (exerciseIds != null)
                ? exerciseService.getExercisesByIds(exerciseIds)
                : null;
    }
}
