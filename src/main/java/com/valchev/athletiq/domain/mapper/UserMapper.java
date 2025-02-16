package com.valchev.athletiq.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.valchev.athletiq.domain.dto.UserDTO;
import com.valchev.athletiq.domain.entity.User;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "savedWorkouts", target = "savedWorkouts")
    UserDTO toDTO(User user);

    @Mapping(target = "userId", ignore = true)
    @Mapping(source = "savedWorkouts", target = "savedWorkouts", ignore = true)
    User toEntity(UserDTO userDTO);

}
