package com.example.delivery_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Set;

public record ScenarioRequestDTO(
        @NotBlank(message = "Scenario name is required")
        String name,

        @NotNull(message = "Vehicle IDs are required")
        Set<@NotNull @Positive Long> vehicleIds,

        @NotNull(message = "Delivery IDs are required")
        Set<@NotNull @Positive Long> deliveryIds
) {
}