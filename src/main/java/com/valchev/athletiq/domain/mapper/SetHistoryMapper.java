package com.valchev.athletiq.domain.mapper;

import com.valchev.athletiq.domain.dto.ExerciseSetHistoryDTO;
import com.valchev.athletiq.domain.entity.ExerciseSetHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SetHistoryMapper {

    @Mapping(target = "exerciseHistory", ignore = true)
    ExerciseSetHistory toEntity(ExerciseSetHistoryDTO dto);

    ExerciseSetHistoryDTO toDTO(ExerciseSetHistory entity);

    List<ExerciseSetHistoryDTO> toDTOs(List<ExerciseSetHistory> entities);
}
