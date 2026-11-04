package com.example.delivery_system.dto;

public record VehicleResponseDTO(
        Long id,
        String name,
        Integer capacity,
        Boolean available,
        Long depotId
) {
}