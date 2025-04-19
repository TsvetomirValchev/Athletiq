package com.valchev.athletiq.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgottenPasswordDTO {
    @NotBlank(message = "Email is required")
    @Email(message = "Valid email is required")
    private String email;
}