package com.example.delivery_system.controller;

import com.example.delivery_system.dto.DeliveryRequestDTO;
import com.example.delivery_system.dto.DeliveryResponseDTO;
import com.example.delivery_system.service.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
@Tag(name = "Deliveries", description = "Delivery management")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Operation(summary = "Retrieve all deliveries")
    @GetMapping
    public List<DeliveryResponseDTO> getAllDeliveries() {
        return deliveryService.findAll();
    }

    @Operation(summary = "Retrieve a delivery by ID")
    @GetMapping("/{id}")
    public DeliveryResponseDTO getDelivery(@PathVariable Long id) {
        return deliveryService.findById(id);
    }

    @Operation(summary = "Create a delivery")
    @PostMapping
    public ResponseEntity<DeliveryResponseDTO> createDelivery(
            @Valid @RequestBody DeliveryRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(deliveryService.create(dto));
    }

    @Operation(summary = "Update a delivery")
    @PutMapping("/{id}")
    public DeliveryResponseDTO updateDelivery(
            @PathVariable Long id,
            @Valid @RequestBody DeliveryRequestDTO dto) {

        return deliveryService.update(id, dto);
    }

    @Operation(summary = "Delete a delivery")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDelivery(@PathVariable Long id) {
        deliveryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}