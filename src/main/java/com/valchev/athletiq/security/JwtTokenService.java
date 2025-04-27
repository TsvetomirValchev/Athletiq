package com.valchev.athletiq.security;

import com.valchev.athletiq.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtEncoder encoder;
    private final CustomUserDetailsService userDetailsService;

    public String generateToken(Authentication authentication) {
        return generateToken(authentication, 1, ChronoUnit.MINUTES);
    }

    public String generateMobileToken(Authentication authentication) {
        return generateToken(authentication, 30, ChronoUnit.DAYS);
    }

    public String generateTokenForUser(String username, boolean isMobile) {
        AthletiqUser athletiqUser = (AthletiqUser) userDetailsService.loadUserByUsername(username);

        Instant now = Instant.now();

        Instant expiration = isMobile ?
                now.plus(30, ChronoUnit.DAYS) :
                now.plus(1, ChronoUnit.MINUTES);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(expiration)
                .subject(username)
                .claim("userId", athletiqUser.getUserId().toString())
                .build();

        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    // Flexible token generation with custom duration
    public String generateToken(Authentication authentication, long amount, ChronoUnit unit) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        UUID userId = null;
        if (authentication.getPrincipal() instanceof AthletiqUser) {
            userId = ((AthletiqUser) authentication.getPrincipal()).getUserId();
        }

        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(amount, unit))
                .subject(authentication.getName())
                .claim("scope", scope);

        if (userId != null) {
            claimsBuilder.claim("userId", userId);
        }

        return this.encoder.encode(JwtEncoderParameters.from(claimsBuilder.build())).getTokenValue();
    }
}