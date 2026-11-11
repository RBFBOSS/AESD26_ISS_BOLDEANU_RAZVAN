
package com.example.delivery_system.dto;

public record AuthResponseDTO(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn,
        String role
) {
}
