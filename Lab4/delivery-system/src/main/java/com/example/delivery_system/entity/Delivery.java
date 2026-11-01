package com.example.delivery_system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")
@Getter
@Setter
@NoArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private Integer quantity;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    public Delivery(Customer customer, Integer quantity, String notes) {
        this.customer = customer;
        this.quantity = quantity;
        this.notes = notes;
        this.timestamp = LocalDateTime.now();
    }
}