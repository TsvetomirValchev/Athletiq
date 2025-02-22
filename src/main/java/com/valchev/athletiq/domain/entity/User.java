package com.valchev.athletiq.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Entity(name = "atlethiq_user")
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
