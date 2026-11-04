package com.example.delivery_system.repository;

import com.example.delivery_system.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    // Derived query
    List<Vehicle> findByAvailableTrue();

    // Explicit JPQL query
    @Query("SELECT v FROM Vehicle v WHERE v.depot.name = :depotName")
    List<Vehicle> findVehiclesByDepotName(
            @Param("depotName") String depotName
    );

    // Transactional modifying query
    @Modifying
    @Transactional
    @Query("UPDATE Vehicle v SET v.available = :available WHERE v.id = :id")
    int updateAvailability(
            @Param("id") Long id,
            @Param("available") boolean available
    );
}