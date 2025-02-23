package com.valchev.athletiq.domain.mapper;


import com.valchev.athletiq.domain.dto.UserDTO;
import com.valchev.athletiq.domain.entity.User;
import com.valchev.athletiq.domain.entity.Workout;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "savedWorkouts", target = "savedWorkoutIds", qualifiedByName = "mapWorkoutIds")
    UserDTO toDTO(User user);

    List<UserDTO> toDTOs(List<User> users);

    default List<UUID> mapWorkoutIds(List<Workout> workouts) {
        return workouts != null ? workouts.stream().map(Workout::getWorkoutId).toList() : null;
    }

    @Mapping(target = "userId", ignore = true)
    @Mapping(source = "savedWorkouts", target = "savedWorkouts", ignore = true)
    User toEntity(UserDTO userDTO);


}
