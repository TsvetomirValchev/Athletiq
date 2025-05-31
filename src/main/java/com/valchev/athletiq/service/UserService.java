package com.valchev.athletiq.service;

import com.valchev.athletiq.domain.dto.UserDTO;
import com.valchev.athletiq.domain.entity.User;
import com.valchev.athletiq.domain.exception.NoSuchUserException;
import com.valchev.athletiq.domain.exception.ResourceNotFoundException;
import com.valchev.athletiq.domain.mapper.UserMapper;
import com.valchev.athletiq.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public Optional<UserDTO> getById(UUID userId) {
        return userRepository.findById(userId)
                .map(userMapper::toDTO);
    }

    public UserDTO save(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    public void deleteById(UUID userId) {
        userRepository.deleteById(userId);
    }

    public Optional<UserDTO> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDTO);
    }

    public Optional<UserDTO> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDTO);
    }

    public void updatePassword(UUID userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        user.setPassword(newPassword);
        User updatedUser = userRepository.save(user);
        log.info("Password updated to {} for user: {}", newPassword, userId);

        userMapper.toDTO(updatedUser);
    }

    public boolean isEmail(String input) {
        return input != null && input.contains("@");
    }

    public String findUsernameByEmail(String email) {
        return findByEmail(email)
                .map(UserDTO::getUsername)
                .orElseThrow(() -> new NoSuchUserException("No user found with email: " + email));
    }
}

