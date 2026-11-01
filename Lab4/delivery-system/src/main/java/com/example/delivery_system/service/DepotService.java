package com.example.delivery_system.service;

import com.example.delivery_system.entity.Depot;
import com.example.delivery_system.repository.DepotRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepotService {

    private final DepotRepository repository;

    public DepotService(DepotRepository repository) {
        this.repository = repository;
    }

    public List<Depot> findAll() {
        return repository.findAll();
    }

    public Optional<Depot> findById(Long id) {
        return repository.findById(id);
    }

    public Depot save(Depot depot) {
        return repository.save(depot);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}