package com.valchev.athletiq.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.UUID;

@Getter
public class AthletiqUser extends User {
    private final UUID userId;
    private final String email;

    public AthletiqUser(String username, String password, Collection<? extends GrantedAuthority> authorities, UUID userId, String email) {
        super(username, password, authorities);
        this.userId = userId;
        this.email = email;
    }

}