package com.valchev.athletiq.domain.mapper;

import com.valchev.athletiq.domain.dto.ActiveWorkoutDTO;
import com.valchev.athletiq.domain.dto.WorkoutDTO;
import com.valchev.athletiq.domain.entity.ActiveWorkout;
import com.valchev.athletiq.domain.entity.Exercise;
import com.valchev.athletiq.domain.entity.Workout;
import com.valchev.athletiq.service.ExerciseService;
import org.mapstruct.*;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface WorkoutMapper {

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "exercises", target = "exerciseIds", qualifiedByName = "mapExerciseIds")
    WorkoutDTO toDTO(Workout workout);

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "exercises", target = "exerciseIds", qualifiedByName = "mapExerciseIds")
    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "endTime", target = "endTime")
    @SubclassMapping(source = ActiveWorkout.class, target = ActiveWorkoutDTO.class)
    ActiveWorkoutDTO toDTO(ActiveWorkout workout);

    @Mapping(source = "userId", target = "user.userId")
    @Mapping(source = "exerciseIds", target = "exercises", qualifiedByName = "mapExerciseIdsToExercises")
    @Mapping(target = "workoutId", ignore = true)
    Workout toEntity(WorkoutDTO dto, @Context ExerciseService exerciseService);

    @Mapping(source = "userId", target = "user.userId")
    @Mapping(source = "exerciseIds", target = "exercises", qualifiedByName = "mapExerciseIdsToExercises")
    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "endTime", target = "endTime")
    @SubclassMapping(source = ActiveWorkoutDTO.class, target = ActiveWorkout.class)
    ActiveWorkout toEntity(ActiveWorkoutDTO dto, @Context ExerciseService exerciseService);

    List<WorkoutDTO> toDTOs(List<Workout> workouts);

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



