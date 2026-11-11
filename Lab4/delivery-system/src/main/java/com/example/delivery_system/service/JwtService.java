
package com.example.delivery_system.service;
import org.springframework.security.access.prepost.PreAuthorize;
import com.example.delivery_system.entity.AppUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final long accessTokenSeconds;

    public JwtService(
            JwtEncoder jwtEncoder,
            @Value("${app.jwt.access-token-seconds:900}")
            long accessTokenSeconds) {

        this.jwtEncoder = jwtEncoder;
        this.accessTokenSeconds = accessTokenSeconds;
    }

    public String generateAccessToken(AppUser user) {

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("delivery-system")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(accessTokenSeconds))
                .subject(user.getEmail())
                .claim("roles", List.of(user.getRole().name()))
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256)
                .build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(header, claims)
        ).getTokenValue();
    }

    public long getAccessTokenSeconds() {
        return accessTokenSeconds;
    }
}
