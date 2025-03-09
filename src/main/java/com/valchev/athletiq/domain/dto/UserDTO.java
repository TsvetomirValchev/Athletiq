package com.valchev.athletiq.domain.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserDTO {

    private UUID userId;
    private String email;
    private String username;
    private String password;
    private List<UUID> savedWorkoutIds;

}
