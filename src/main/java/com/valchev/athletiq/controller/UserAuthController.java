package com.valchev.athletiq.controller;

import com.valchev.athletiq.domain.dto.ForgottenPasswordDTO;
import com.valchev.athletiq.domain.dto.LoginRequestDTO;
import com.valchev.athletiq.domain.dto.RegistrationResponseDTO;
import com.valchev.athletiq.domain.dto.ResetPasswordDTO;
import com.valchev.athletiq.domain.dto.UserDTO;
import com.valchev.athletiq.domain.exception.AccessDeniedException;
import com.valchev.athletiq.security.JwtTokenService;
import com.valchev.athletiq.security.PasswordResetService;
import com.valchev.athletiq.service.EmailService;
import com.valchev.athletiq.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@Slf4j
public class UserAuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final PasswordResetService passwordResetService;
    private final EmailService emailService;

    @Autowired
    public UserAuthController(AuthenticationManager authenticationManager, UserService userService,
                              PasswordEncoder passwordEncoder, JwtTokenService jwtTokenService, PasswordResetService passwordResetService, EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
        this.passwordResetService = passwordResetService;
        this.emailService = emailService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginRequestDTO request,
            @RequestHeader(value = "X-Client-Type", defaultValue = "web") String clientType) {
        String username = resolveUsername(request.getUsernameOrEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, request.getPassword()));

        String jwt = generateToken(authentication, clientType);

        return ResponseEntity.ok(jwt);
    }

    private String resolveUsername(String usernameOrEmail) {
        if (userService.isEmail(usernameOrEmail)) {
            log.info("Resolving email to username: {}", usernameOrEmail);
            return userService.findUsernameByEmail(usernameOrEmail);
        }
        return usernameOrEmail;
    }


    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(
            Authentication authentication,
            @RequestHeader(value = "X-Client-Type") String clientType) {

        return ResponseEntity.ok(generateToken(authentication, clientType));
    }

    private String generateToken(Authentication authentication, String clientType) {
        return clientType.equalsIgnoreCase("mobile") ?
                jwtTokenService.generateMobileToken(authentication) :
                jwtTokenService.generateToken(authentication);
    }


    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseDTO> register(@Valid @RequestBody UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        UserDTO savedUser = userService.save(userDTO);

        RegistrationResponseDTO responseDTO = RegistrationResponseDTO.builder()
                .userId(savedUser.getUserId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid ForgottenPasswordDTO request) {
        String email = request.getEmail();

        userService.findByEmail(email).ifPresent(user -> {
            String token = passwordResetService.createToken(user);
            emailService.sendPasswordResetEmail(user.getEmail(), token);
        });

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordDTO request) {
        UserDTO user = passwordResetService.validateToken(request.getToken());
        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        userService.updatePassword(user.getUserId(), encodedPassword);
        passwordResetService.invalidateToken(request.getToken());
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("id") String id,
            Authentication authentication) {
        UUID userId = UUID.fromString(id);

        Jwt jwt = (Jwt) authentication.getPrincipal();
        String username = jwt.getSubject();

        UserDTO currentUser = userService.findByUsername(username)
                .orElseThrow(() -> new AccessDeniedException("User not found"));

        if (!userId.equals(currentUser.getUserId())) {
            throw new AccessDeniedException("You can only delete your own account");
        }

        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }
}