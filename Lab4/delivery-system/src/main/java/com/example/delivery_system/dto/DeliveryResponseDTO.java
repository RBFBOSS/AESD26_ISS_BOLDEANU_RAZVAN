package com.example.delivery_system.dto;

import java.time.LocalDateTime;

public record DeliveryResponseDTO(
        Long id,
        Long customerId,
        String customerName,
        Integer quantity,
        String notes,
        LocalDateTime timestamp
) {
}