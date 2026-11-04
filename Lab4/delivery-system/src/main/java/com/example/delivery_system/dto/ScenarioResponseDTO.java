package com.example.delivery_system.dto;

import java.util.Set;

public record ScenarioResponseDTO(
        Long id,
        String name,
        Set<Long> vehicleIds,
        Set<Long> deliveryIds
) {
}