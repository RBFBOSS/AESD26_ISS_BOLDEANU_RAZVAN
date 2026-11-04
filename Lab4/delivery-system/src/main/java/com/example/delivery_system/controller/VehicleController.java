package com.example.delivery_system.controller;

import com.example.delivery_system.dto.VehicleRequestDTO;
import com.example.delivery_system.dto.VehicleResponseDTO;
import com.example.delivery_system.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@Tag(name = "Vehicles", description = "Vehicle management")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    private String createETag(VehicleResponseDTO vehicle) {
        return "\"" + Integer.toHexString(vehicle.hashCode()) + "\"";
    }

    private boolean matchesETag(String ifNoneMatch, String etag) {
        if (ifNoneMatch == null) {
            return false;
        }

        for (String value : ifNoneMatch.split(",")) {
            String candidate = value.trim();

            if (candidate.equals("*") ||
                    candidate.equals(etag) ||
                    candidate.equals("W/" + etag)) {
                return true;
            }
        }

        return false;
    }

    @Operation(summary = "Retrieve all vehicles")
    @GetMapping(produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    public List<VehicleResponseDTO> getAllVehicles() {
        return vehicleService.findAll();
    }

    @Operation(summary = "Retrieve a vehicle by ID with ETag support")
    @GetMapping(value = "/{id}", produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE
    })
    public ResponseEntity<VehicleResponseDTO> getVehicle(
            @PathVariable Long id,
            @RequestHeader(value = HttpHeaders.IF_NONE_MATCH, required = false)
            String ifNoneMatch) {

        VehicleResponseDTO vehicle = vehicleService.findById(id);
        String etag = createETag(vehicle);

        if (matchesETag(ifNoneMatch, etag)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .eTag(etag)
                    .build();
        }

        return ResponseEntity.ok()
                .eTag(etag)
                .body(vehicle);
    }

    @Operation(summary = "Create a vehicle")
    @PostMapping
    public ResponseEntity<VehicleResponseDTO> createVehicle(
            @Valid @RequestBody VehicleRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vehicleService.create(dto));
    }

    @Operation(summary = "Update a vehicle")
    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponseDTO> updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody VehicleRequestDTO dto) {

        return ResponseEntity.ok(vehicleService.update(id, dto));
    }

    @Operation(summary = "Delete a vehicle")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}