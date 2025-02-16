package com.valchev.athletiq.domain.entity;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Entity(name = "user")
@Slf4j
@Data
public class User {

    @Id
    private UUID userId;
    private String email;
    private String password;
    private String username;

    @OneToMany(mappedBy = "user")
    private List<Workout> savedWorkouts;

}
