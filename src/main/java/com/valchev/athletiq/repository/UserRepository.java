package com.valchev.athletiq.repository;

import com.valchev.athletiq.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
