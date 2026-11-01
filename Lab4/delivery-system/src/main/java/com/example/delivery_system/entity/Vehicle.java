package com.example.delivery_system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer capacity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "depot", nullable = false)
    private Depot depot;

    @Column(nullable = false)
    private Boolean available = true;

    @OneToOne
    @JoinColumn(name = "driver", unique = true)
    private Driver driver;

    public Vehicle(String name, Integer capacity, Depot depot) {
        this.name = name;
        this.capacity = capacity;
        this.depot = depot;
        this.available = true;
    }
}