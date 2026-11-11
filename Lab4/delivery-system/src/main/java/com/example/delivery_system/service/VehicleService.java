
package com.example.delivery_system.service;

import com.example.delivery_system.dto.VehicleRequestDTO;
import com.example.delivery_system.dto.VehicleResponseDTO;
import com.example.delivery_system.entity.Depot;
import com.example.delivery_system.entity.Vehicle;
import com.example.delivery_system.exception.ResourceNotFoundException;
import com.example.delivery_system.repository.DepotRepository;
import com.example.delivery_system.repository.VehicleRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DepotRepository depotRepository;

    public VehicleService(
            VehicleRepository vehicleRepository,
            DepotRepository depotRepository) {
        this.vehicleRepository = vehicleRepository;
        this.depotRepository = depotRepository;
    }

    private VehicleResponseDTO toDTO(Vehicle vehicle) {
        return new VehicleResponseDTO(
                vehicle.getId(),
                vehicle.getName(),
                vehicle.getCapacity(),
                vehicle.getAvailable(),
                vehicle.getDepot().getId()
        );
    }

    private Vehicle getVehicle(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vehicle not found: " + id));
    }

    public List<VehicleResponseDTO> findAll() {
        return vehicleRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public VehicleResponseDTO findById(Long id) {
        return toDTO(getVehicle(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public VehicleResponseDTO create(VehicleRequestDTO dto) {

        Depot depot = depotRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException("No depot exists"));

        Vehicle vehicle = new Vehicle(
                dto.name(),
                dto.capacity(),
                depot
        );

        vehicle.setAvailable(
                dto.available() == null ? true : dto.available()
        );

        return toDTO(vehicleRepository.save(vehicle));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public VehicleResponseDTO update(Long id, VehicleRequestDTO dto) {

        Vehicle vehicle = getVehicle(id);

        vehicle.setName(dto.name());
        vehicle.setCapacity(dto.capacity());

        if (dto.available() != null) {
            vehicle.setAvailable(dto.available());
        }

        return toDTO(vehicleRepository.save(vehicle));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delete(Long id) {
        vehicleRepository.delete(getVehicle(id));
    }

    public List<VehicleResponseDTO> findAvailableVehicles() {
        return vehicleRepository.findByAvailableTrue()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public int updateAvailability(Long id, boolean available) {

        int updated = vehicleRepository.updateAvailability(id, available);

        if (updated == 0) {
            throw new ResourceNotFoundException(
                    "Vehicle not found: " + id);
        }

        return updated;
    }

    public List<VehicleResponseDTO> findByDepotName(String depotName) {
        return vehicleRepository.findVehiclesByDepotName(depotName)
                .stream()
                .map(this::toDTO)
                .toList();
    }
}
