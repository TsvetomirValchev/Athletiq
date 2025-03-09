package com.valchev.athletiq.controller;

import com.valchev.athletiq.domain.dto.LoginRequestDTO;
import com.valchev.athletiq.domain.dto.UserDTO;
import com.valchev.athletiq.security.JwtUtil;
import com.valchev.athletiq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class UserAuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserAuthController(AuthenticationManager authenticationManager, UserService userService,
                              PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        String jwt = jwtUtil.generateToken(request.getUsername());
        return ResponseEntity.ok("Bearer " + jwt);
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") String id,
                                             @AuthenticationPrincipal UserDetails currentUser) {
        UUID userId = UUID.fromString(id);
        Optional<UserDTO> optionalUserDTO = userService.getById(userId);
        if (optionalUserDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        UserDTO userDTO = optionalUserDTO.get();
        if (!userDTO.getUsername().equals(currentUser.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only delete your own account");
        }
        userService.deleteById(userId);
        return ResponseEntity.ok("User deleted successfully");
    }
}
