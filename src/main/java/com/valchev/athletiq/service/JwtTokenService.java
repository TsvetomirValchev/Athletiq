package com.valchev.athletiq.service;

import com.valchev.athletiq.security.AthletiqUser;
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
    private final CustomUserDetailsService userDetailsService;

    public String generateToken(Authentication authentication) {
        return generateToken(authentication, 1, ChronoUnit.DAYS);
    }

    public String generateMobileToken(Authentication authentication) {
        return generateToken(authentication, 30, ChronoUnit.DAYS);
    }

    public String generateToken(Authentication authentication, long amount, ChronoUnit unit) {
        Instant now = Instant.now();

        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        String username = authentication.getName();
        AthletiqUser athletiqUser = (AthletiqUser) userDetailsService.loadUserByUsername(username);


        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(amount, unit))
                .subject(authentication.getName())
                .claim("userId", athletiqUser.getUserId().toString())
                .claim("username", athletiqUser.getUsername())
                .claim("email", athletiqUser.getEmail())
                .claim("scope", scope);

        return this.encoder.encode(JwtEncoderParameters.from(claimsBuilder.build())).getTokenValue();
    }
}