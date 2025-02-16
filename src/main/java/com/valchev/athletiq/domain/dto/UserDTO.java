package com.valchev.athletiq.domain.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {

    private String email;
    private String username;
    private String password;
    private List<WorkoutDTO> savedWorkouts;

}
