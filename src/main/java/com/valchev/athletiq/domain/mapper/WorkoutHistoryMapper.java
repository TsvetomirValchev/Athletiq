package com.valchev.athletiq.domain.mapper;

import com.valchev.athletiq.domain.dto.WorkoutHistoryDTO;
import com.valchev.athletiq.domain.entity.WorkoutHistory;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ExerciseHistoryMapper.class})
public interface WorkoutHistoryMapper {

    @Mapping(source = "workoutHistoryId", target = "workoutHistoryId")
    WorkoutHistory toEntity(WorkoutHistoryDTO dto);

    @Mapping(source = "workoutHistoryId", target = "workoutHistoryId")
    @Mapping(source = "exerciseHistories", target = "exerciseHistories")
    WorkoutHistoryDTO toDTO(WorkoutHistory entity);

    List<WorkoutHistoryDTO> toDTOs(List<WorkoutHistory> entities);

    @Named("toSummaryDTO")
    @Mapping(source = "workoutHistoryId", target = "workoutHistoryId")
    @Mapping(target = "exerciseHistories", ignore = true)
    WorkoutHistoryDTO toSummaryDTO(WorkoutHistory entity);

    @IterableMapping(qualifiedByName = "toSummaryDTO")
    List<WorkoutHistoryDTO> toSummaryDTOs(List<WorkoutHistory> entities);
}
