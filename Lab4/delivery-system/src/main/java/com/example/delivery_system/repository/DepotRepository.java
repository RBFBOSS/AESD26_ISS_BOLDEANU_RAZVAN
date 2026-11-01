package com.example.delivery_system.repository;

import com.example.delivery_system.entity.Depot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepotRepository extends JpaRepository<Depot, Long> {
    Optional<Depot> findByName(String name);
}