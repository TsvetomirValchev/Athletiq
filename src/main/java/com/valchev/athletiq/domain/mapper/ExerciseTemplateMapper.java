package com.valchev.athletiq.domain.mapper;

import com.valchev.athletiq.domain.dto.ExerciseTemplateDTO;
import com.valchev.athletiq.domain.entity.ExerciseTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExerciseTemplateMapper {

    ExerciseTemplateDTO toDTO(ExerciseTemplate exerciseTemplate);

    @Mapping(target = "exerciseTemplateId", ignore = true)
    ExerciseTemplate toEntity(ExerciseTemplateDTO exerciseTemplateDTO);

    List<ExerciseTemplateDTO> toDTOList(List<ExerciseTemplate> exerciseTemplates);
}