package com.example.delivery_system.service;

import com.example.delivery_system.entity.Vehicle;
import com.example.delivery_system.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final VehicleRepository repository;

    public VehicleService(VehicleRepository repository) {
        this.repository = repository;
    }

    public List<Vehicle> findAll() {
        return repository.findAll();
    }

    public Optional<Vehicle> findById(Long id) {
        return repository.findById(id);
    }

    public List<Vehicle> findAvailableVehicles() {
        return repository.findByAvailableTrue();
    }

    public List<Vehicle> findByDepotName(String depotName) {
        return repository.findVehiclesByDepotName(depotName);
    }

    public Vehicle save(Vehicle vehicle) {
        return repository.save(vehicle);
    }

    public int updateAvailability(Long id, boolean available) {
        return repository.updateAvailability(id, available);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}