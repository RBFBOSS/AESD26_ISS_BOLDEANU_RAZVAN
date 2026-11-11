
package com.example.delivery_system.service;

import com.example.delivery_system.dto.ScenarioRequestDTO;
import com.example.delivery_system.dto.ScenarioResponseDTO;
import com.example.delivery_system.entity.Delivery;
import com.example.delivery_system.entity.Scenario;
import com.example.delivery_system.entity.Vehicle;
import com.example.delivery_system.exception.ResourceNotFoundException;
import com.example.delivery_system.repository.DeliveryRepository;
import com.example.delivery_system.repository.ScenarioRepository;
import com.example.delivery_system.repository.VehicleRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScenarioService {

    private final ScenarioRepository scenarioRepository;
    private final VehicleRepository vehicleRepository;
    private final DeliveryRepository deliveryRepository;

    public ScenarioService(
            ScenarioRepository scenarioRepository,
            VehicleRepository vehicleRepository,
            DeliveryRepository deliveryRepository) {
        this.scenarioRepository = scenarioRepository;
        this.vehicleRepository = vehicleRepository;
        this.deliveryRepository = deliveryRepository;
    }

    private ScenarioResponseDTO toDTO(Scenario scenario) {
        return new ScenarioResponseDTO(
                scenario.getId(),
                scenario.getName(),
                scenario.getVehicles().stream()
                        .map(Vehicle::getId)
                        .collect(Collectors.toSet()),
                scenario.getDeliveries().stream()
                        .map(Delivery::getId)
                        .collect(Collectors.toSet())
        );
    }

    private Scenario getScenario(Long id) {
        return scenarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Scenario not found: " + id));
    }

    private Set<Vehicle> getVehicles(Set<Long> ids) {
        Set<Vehicle> vehicles = vehicleRepository.findAllById(ids)
                .stream()
                .collect(Collectors.toSet());

        if (vehicles.size() != ids.size()) {
            throw new ResourceNotFoundException(
                    "One or more vehicles do not exist");
        }

        return vehicles;
    }

    private Set<Delivery> getDeliveries(Set<Long> ids) {
        Set<Delivery> deliveries = deliveryRepository.findAllById(ids)
                .stream()
                .collect(Collectors.toSet());

        if (deliveries.size() != ids.size()) {
            throw new ResourceNotFoundException(
                    "One or more deliveries do not exist");
        }

        return deliveries;
    }

    public List<ScenarioResponseDTO> findAll() {
        return scenarioRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public ScenarioResponseDTO findById(Long id) {
        return toDTO(getScenario(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ScenarioResponseDTO create(ScenarioRequestDTO dto) {

        Scenario scenario = new Scenario(dto.name());

        scenario.setVehicles(getVehicles(dto.vehicleIds()));
        scenario.setDeliveries(getDeliveries(dto.deliveryIds()));

        return toDTO(scenarioRepository.save(scenario));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ScenarioResponseDTO update(Long id, ScenarioRequestDTO dto) {

        Scenario scenario = getScenario(id);

        scenario.setName(dto.name());
        scenario.setVehicles(getVehicles(dto.vehicleIds()));
        scenario.setDeliveries(getDeliveries(dto.deliveryIds()));

        return toDTO(scenarioRepository.save(scenario));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delete(Long id) {
        scenarioRepository.delete(getScenario(id));
    }
}
