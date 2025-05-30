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

    ExerciseSetDTO toDTO(ExerciseSet exerciseSet);

    @Mapping(target = "exercise", ignore = true)
    ExerciseSet toEntity(ExerciseSetDTO exerciseSetDTO);

    List<ExerciseSetDTO> toDTOs(List<ExerciseSet> exerciseSets);

    List<ExerciseSet> toEntities(List<ExerciseSetDTO> exerciseSetDTOs);

    @AfterMapping
    default void setExerciseReference(@MappingTarget ExerciseSet set, ExerciseSetDTO dto) {
        if (dto.getExerciseId() != null) {
            Exercise exercise = new Exercise();
            exercise.setExerciseId(dto.getExerciseId());
            set.setExercise(exercise);
        }
    }
}