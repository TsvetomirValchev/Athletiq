package com.valchev.athletiq.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String username;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    @NotNull
    @NotBlank
    private String password;

    private List<UUID> savedWorkoutIds;

}
