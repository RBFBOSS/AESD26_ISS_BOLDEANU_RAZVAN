
package com.example.delivery_system.service;
import org.springframework.security.access.prepost.PreAuthorize;
import com.example.delivery_system.dto.*;
import com.example.delivery_system.entity.*;
import com.example.delivery_system.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Locale;

@Service
public class AuthService {

    private final AppUserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SecureRandom secureRandom = new SecureRandom();

    private final long refreshTokenSeconds;

    public AuthService(
            AppUserRepository userRepository,
            CustomerRepository customerRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            @Value("${app.jwt.refresh-token-seconds:604800}")
            long refreshTokenSeconds) {

        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenSeconds = refreshTokenSeconds;
    }

    @Transactional
    public RegisterResponseDTO register(RegisterRequestDTO dto) {

        String email = normalizeEmail(dto.email());

        if (userRepository.existsByEmail(email)
                || customerRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email is already registered"
            );
        }

        AppUser user = new AppUser(
                email,
                passwordEncoder.encode(dto.password()),
                Role.CUSTOMER
        );

        user = userRepository.save(user);

        Customer customer = new Customer(
                dto.name(),
                email,
                dto.phone(),
                dto.address(),
                dto.coordinates()
        );

        customer.setUser(user);
        customer = customerRepository.save(customer);

        return new RegisterResponseDTO(
                user.getId(),
                customer.getId(),
                email,
                user.getRole().name()
        );
    }

    @Transactional
    public AuthResponseDTO login(LoginRequestDTO dto) {

        AppUser user = userRepository
                .findByEmail(normalizeEmail(dto.email()))
                .orElseThrow(() ->
                        new BadCredentialsException(
                                "Invalid email or password"
                        ));

        if (!passwordEncoder.matches(
                dto.password(), user.getPassword())) {

            throw new BadCredentialsException(
                    "Invalid email or password"
            );
        }

        return issueTokens(user);
    }

    @Transactional
    public AuthResponseDTO refresh(RefreshTokenRequestDTO dto) {

        String hash = hashToken(dto.refreshToken());

        RefreshToken stored = refreshTokenRepository
                .findByTokenHash(hash)
                .orElseThrow(() ->
                        new BadCredentialsException(
                                "Invalid refresh token"
                        ));

        if (stored.isRevoked()
                || !stored.getExpiresAt().isAfter(Instant.now())) {

            throw new BadCredentialsException(
                    "Expired or revoked refresh token"
            );
        }

        // A refresh token may be used only once.
        stored.setRevoked(true);
        refreshTokenRepository.save(stored);

        return issueTokens(stored.getUser());
    }

    @Transactional
    public void logout(RefreshTokenRequestDTO dto) {

        String hash = hashToken(dto.refreshToken());

        refreshTokenRepository.findByTokenHash(hash)
                .ifPresent(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                });
    }

    private AuthResponseDTO issueTokens(AppUser user) {

        String accessToken =
                jwtService.generateAccessToken(user);

        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        String refreshToken = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);

        RefreshToken stored = new RefreshToken(
                hashToken(refreshToken),
                user,
                Instant.now().plusSeconds(refreshTokenSeconds)
        );

        refreshTokenRepository.save(stored);

        return new AuthResponseDTO(
                accessToken,
                refreshToken,
                "Bearer",
                jwtService.getAccessTokenSeconds(),
                user.getRole().name()
        );
    }

    private String hashToken(String token) {

        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    token.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (Exception ex) {
            throw new IllegalStateException(
                    "Cannot hash refresh token", ex
            );
        }
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
