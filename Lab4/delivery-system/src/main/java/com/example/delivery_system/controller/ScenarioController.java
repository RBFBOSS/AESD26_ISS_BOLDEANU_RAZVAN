package com.example.delivery_system.controller;

import com.example.delivery_system.dto.ScenarioRequestDTO;
import com.example.delivery_system.dto.ScenarioResponseDTO;
import com.example.delivery_system.service.ScenarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scenarios")
@Tag(name = "Scenarios", description = "Vehicle routing scenario management")
public class ScenarioController {

    private final ScenarioService scenarioService;

    public ScenarioController(ScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }

    @Operation(summary = "Retrieve all scenarios")
    @GetMapping
    public List<ScenarioResponseDTO> getAllScenarios() {
        return scenarioService.findAll();
    }

    @Operation(summary = "Retrieve a scenario by ID")
    @GetMapping("/{id}")
    public ScenarioResponseDTO getScenario(@PathVariable Long id) {
        return scenarioService.findById(id);
    }

    @Operation(summary = "Create a scenario with vehicles and deliveries")
    @PostMapping
    public ResponseEntity<ScenarioResponseDTO> createScenario(
            @Valid @RequestBody ScenarioRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(scenarioService.create(dto));
    }

    @Operation(summary = "Update a scenario")
    @PutMapping("/{id}")
    public ScenarioResponseDTO updateScenario(
            @PathVariable Long id,
            @Valid @RequestBody ScenarioRequestDTO dto) {

        return scenarioService.update(id, dto);
    }

    @Operation(summary = "Delete a scenario")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScenario(@PathVariable Long id) {
        scenarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}