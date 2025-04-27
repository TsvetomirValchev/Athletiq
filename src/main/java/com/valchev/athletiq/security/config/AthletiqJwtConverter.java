package com.valchev.athletiq.security.config;

import com.valchev.athletiq.security.AthletiqUser;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.UUID;

@Component
public class AthletiqJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = jwtGrantedAuthoritiesConverter.convert(jwt);

        if (authorities.stream().noneMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            (authorities).add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        String userIdStr = jwt.getClaimAsString("userId");
        UUID userId = null;
        if (userIdStr != null) {
            try {
                userId = UUID.fromString(userIdStr);
            } catch (IllegalArgumentException e) {
                System.out.println("Warning: Invalid userId format in JWT: " + userIdStr);
            }
        }

        return getJwtAuthenticationToken(jwt, authorities, userId);
    }

    private static JwtAuthenticationToken getJwtAuthenticationToken(Jwt jwt, Collection<GrantedAuthority> authorities, UUID userId) {
        AthletiqUser user = new AthletiqUser(
                jwt.getSubject(),
                "",
                authorities,
                userId
        );

        JwtAuthenticationToken token = new JwtAuthenticationToken(
                jwt,
                authorities,
                jwt.getSubject()
        );

        token.setDetails(user);
        return token;
    }
}