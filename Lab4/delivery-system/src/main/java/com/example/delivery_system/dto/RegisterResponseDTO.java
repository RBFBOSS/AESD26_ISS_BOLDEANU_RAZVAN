
package com.example.delivery_system.dto;

public record RegisterResponseDTO(
        Long userId,
        Long customerId,
        String email,
        String role
) {
}
