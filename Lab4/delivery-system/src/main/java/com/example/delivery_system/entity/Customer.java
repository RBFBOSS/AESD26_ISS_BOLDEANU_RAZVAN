package com.example.delivery_system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 30)
    private String phone;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false, length = 100)
    private String coordinates;

    @OneToMany(mappedBy = "customer")
    private List<Delivery> deliveries = new ArrayList<>();

    public Customer(String name, String email, String phone,
                    String address, String coordinates) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.coordinates = coordinates;
    }
}