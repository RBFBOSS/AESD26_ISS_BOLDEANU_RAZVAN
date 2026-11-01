package com.example.delivery_system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "depots")
@Getter
@Setter
@NoArgsConstructor
public class Depot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String coordinates;

    @OneToMany(mappedBy = "depot")
    private List<Vehicle> vehicles = new ArrayList<>();

    public Depot(String name, String coordinates) {
        this.name = name;
        this.coordinates = coordinates;
    }
}