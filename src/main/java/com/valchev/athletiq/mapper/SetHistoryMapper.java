package com.valchev.athletiq.mapper;

import com.valchev.athletiq.domain.dto.SetHistoryDTO;
import com.valchev.athletiq.domain.entity.SetHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SetHistoryMapper {

    @Mapping(target = "exerciseHistory", ignore = true)
    SetHistory toEntity(SetHistoryDTO dto);

    SetHistoryDTO toDTO(SetHistory entity);

    List<SetHistoryDTO> toDTOs(List<SetHistory> entities);
}
