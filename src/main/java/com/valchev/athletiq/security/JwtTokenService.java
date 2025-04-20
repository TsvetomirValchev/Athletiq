package com.valchev.athletiq.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtEncoder encoder;

    public String generateToken(Authentication authentication) {
        return generateToken(authentication, 1, ChronoUnit.MINUTES);
    }

    public String generateMobileToken(Authentication authentication) {
        return generateToken(authentication, 30, ChronoUnit.DAYS);
    }

    // For refresh: generate token for specific username
    public String generateTokenForUser(String username, boolean isMobile) {
        Instant now = Instant.now();

        // Choose expiration based on client type
        Instant expiration = isMobile ?
                now.plus(30, ChronoUnit.DAYS) :
                now.plus(1, ChronoUnit.MINUTES);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(expiration)
                .subject(username)
                .claim("scope", "ROLE_USER")
                .build();

        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    // Flexible token generation with custom duration
    public String generateToken(Authentication authentication, long amount, ChronoUnit unit) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(amount, unit))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();

        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
