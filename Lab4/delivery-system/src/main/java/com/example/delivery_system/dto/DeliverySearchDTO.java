package com.example.delivery_system.dto;
import java.time.LocalDate;
public record DeliverySearchDTO(Long customerId, String area, Integer minQuantity, LocalDate date) { }
