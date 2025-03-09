package com.valchev.athletiq.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.valchev.athletiq.domain.dto.UserDTO;
import com.valchev.athletiq.domain.entity.User;
import com.valchev.athletiq.domain.mapper.UserMapper;
import com.valchev.athletiq.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserDTO> getById(UUID userId) {
        return userRepository.findById(userId)
                .map(userMapper::toDTO);
    }

    public void save(UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = userMapper.toEntity(userDTO);
        User savedUser = userRepository.save(user);
        userMapper.toDTO(savedUser);
    }

    public void deleteById(UUID userId) {
        userRepository.deleteById(userId);
    }

}

