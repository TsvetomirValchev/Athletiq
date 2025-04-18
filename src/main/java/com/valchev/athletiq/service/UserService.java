package com.valchev.athletiq.service;

import java.util.Optional;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.valchev.athletiq.domain.dto.UserDTO;
import com.valchev.athletiq.domain.entity.User;
import com.valchev.athletiq.domain.mapper.UserMapper;
import com.valchev.athletiq.repository.UserRepository;

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

    public void save(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        User savedUser = userRepository.save(user);
        log.info("aloooo {}", savedUser);
        userMapper.toDTO(savedUser);
    }

    public void deleteById(UUID userId) {
        userRepository.deleteById(userId);
    }

    public Optional<UserDTO> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDTO);
    }

}

