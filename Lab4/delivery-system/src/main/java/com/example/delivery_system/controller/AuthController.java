
package com.example.delivery_system.controller;

import com.example.delivery_system.dto.*;
import com.example.delivery_system.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

    @PostMapping("/login")
    public AuthResponseDTO login(
            @Valid @RequestBody LoginRequestDTO request) {

        return authService.login(request);
    }

    @PostMapping("/refresh")
    public AuthResponseDTO refresh(
            @Valid @RequestBody RefreshTokenRequestDTO request) {

        return authService.refresh(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Valid @RequestBody RefreshTokenRequestDTO request) {

        authService.logout(request);
        return ResponseEntity.noContent().build();
    }
}
