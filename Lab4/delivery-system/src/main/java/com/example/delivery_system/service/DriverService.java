package com.example.delivery_system.service;
import org.springframework.security.access.prepost.PreAuthorize;
import com.example.delivery_system.entity.Driver;
import com.example.delivery_system.repository.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DriverService {

    private final DriverRepository repository;

    public DriverService(DriverRepository repository) {
        this.repository = repository;
    }

    public List<Driver> findAll() {
        return repository.findAll();
    }

    public Optional<Driver> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<Driver> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Driver save(Driver driver) {
        return repository.save(driver);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}