package com.example.delivery_system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "drivers")
@Getter
@Setter
@NoArgsConstructor
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 30)
    private String phone;

    @OneToOne(mappedBy = "driver")
    private Vehicle vehicle;

    public Driver(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}