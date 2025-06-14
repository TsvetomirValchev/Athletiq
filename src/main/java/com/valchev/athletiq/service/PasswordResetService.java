package com.valchev.athletiq.service;

import com.valchev.athletiq.domain.dto.PasswordResetTokenDTO;
import com.valchev.athletiq.domain.dto.UserDTO;
import com.valchev.athletiq.domain.exception.InvalidTokenException;
import com.valchev.athletiq.domain.exception.ResourceNotFoundException;
import com.valchev.athletiq.domain.exception.TokenExpiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PasswordResetService {

    private final UserService userService;
    private final Map<String, PasswordResetTokenDTO> tokens = new ConcurrentHashMap<>();

    @Autowired
    public PasswordResetService(UserService userService) {
        this.userService = userService;
    }

    public String createToken(UserDTO user) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(10);

        tokens.put(token, new PasswordResetTokenDTO(user.getUserId(), expiration));
        return token;
    }

    public UserDTO validateToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new InvalidTokenException("Token cannot be empty");
        }

        PasswordResetTokenDTO resetToken = tokens.get(token);
        if (resetToken == null) {
            throw new InvalidTokenException("Invalid password reset token");
        }

        if (LocalDateTime.now().isAfter(resetToken.getExpiration())) {
            tokens.remove(token);
            throw new TokenExpiredException("Password reset token has expired");
        }

        return userService.getById(resetToken.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public void invalidateToken(String token) {
        tokens.remove(token);
    }

}