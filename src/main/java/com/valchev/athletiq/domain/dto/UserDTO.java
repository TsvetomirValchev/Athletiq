package com.valchev.athletiq.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private UUID userId;

    @Email(message = "Please provide a valid email address")
    private String email;

    private String username;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    private List<UUID> savedWorkoutIds;

}
