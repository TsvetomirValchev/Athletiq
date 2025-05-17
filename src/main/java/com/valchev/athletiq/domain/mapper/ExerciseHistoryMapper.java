package com.valchev.athletiq.domain.mapper;

import com.valchev.athletiq.domain.dto.ExerciseHistoryDTO;
import com.valchev.athletiq.domain.entity.ExerciseHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SetHistoryMapper.class})
public interface ExerciseHistoryMapper {

    @Mapping(target = "workoutHistory", ignore = true)
    ExerciseHistory toEntity(ExerciseHistoryDTO dto);

    ExerciseHistoryDTO toDTO(ExerciseHistory entity);

    List<ExerciseHistoryDTO> toDTOs(List<ExerciseHistory> entities);
}
