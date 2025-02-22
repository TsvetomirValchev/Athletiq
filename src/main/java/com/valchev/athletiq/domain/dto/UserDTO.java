package com.valchev.athletiq.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UserDTO {
    private String email;
    private String username;
    private String password;
    private List<UUID> savedWorkouts;
}
