package com.example.delivery_system.service;

import com.example.delivery_system.entity.Delivery;
import com.example.delivery_system.repository.DeliveryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService {

    private final DeliveryRepository repository;

    public DeliveryService(DeliveryRepository repository) {
        this.repository = repository;
    }

    public List<Delivery> findAll() {
        return repository.findAll();
    }

    public Optional<Delivery> findById(Long id) {
        return repository.findById(id);
    }

    public List<Delivery> findByCustomerId(Long customerId) {
        return repository.findByCustomerId(customerId);
    }

    public Delivery save(Delivery delivery) {
        return repository.save(delivery);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}