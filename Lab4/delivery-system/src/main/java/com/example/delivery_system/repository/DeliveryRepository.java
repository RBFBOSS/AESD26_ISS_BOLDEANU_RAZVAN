package com.example.delivery_system.repository;

import com.example.delivery_system.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByCustomerId(Long customerId);
}