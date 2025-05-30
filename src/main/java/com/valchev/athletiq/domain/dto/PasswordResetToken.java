package com.valchev.athletiq.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class PasswordResetToken {
    private UUID userId;
    private LocalDateTime expiration;
}