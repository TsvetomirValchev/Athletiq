package com.valchev.athletiq.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDTO {
    private String email;
    private String username;
    private String password;
    private List<WorkoutDTO> savedWorkouts;
}
