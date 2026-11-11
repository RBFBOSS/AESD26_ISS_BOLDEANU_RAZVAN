
package com.example.delivery_system.security;

import com.example.delivery_system.dto.VehicleRequestDTO;
import com.example.delivery_system.entity.Depot;
import com.example.delivery_system.entity.Vehicle;
import com.example.delivery_system.repository.DepotRepository;
import com.example.delivery_system.repository.VehicleRepository;
import com.example.delivery_system.service.VehicleService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(MethodSecurityTest.TestConfig.class)
public class MethodSecurityTest {

    @Configuration
    @EnableMethodSecurity
    static class TestConfig {

        @Bean
        VehicleRepository vehicleRepository() {
            return mock(VehicleRepository.class);
        }

        @Bean
        DepotRepository depotRepository() {
            return mock(DepotRepository.class);
        }

        @Bean
        VehicleService vehicleService(
                VehicleRepository vehicleRepository,
                DepotRepository depotRepository) {

            return new VehicleService(
                    vehicleRepository,
                    depotRepository
            );
        }
    }

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private DepotRepository depotRepository;

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void customerCannotCreateVehicle() {

        VehicleRequestDTO request =
                new VehicleRequestDTO(
                        "Test Truck",
                        100,
                        true
                );

        assertThrows(
                AccessDeniedException.class,
                () -> vehicleService.create(request)
        );

        verifyNoInteractions(
                vehicleRepository,
                depotRepository
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanCreateVehicle() {

        Depot depot = mock(Depot.class);
        when(depot.getId()).thenReturn(1L);

        when(depotRepository.findAll())
                .thenReturn(List.of(depot));

        when(vehicleRepository.save(any(Vehicle.class)))
                .thenAnswer(invocation -> {
                    Vehicle vehicle = invocation.getArgument(0);
                    Vehicle saved = mock(Vehicle.class);

                    when(saved.getId()).thenReturn(10L);
                    when(saved.getName()).thenReturn(vehicle.getName());
                    when(saved.getCapacity()).thenReturn(vehicle.getCapacity());
                    when(saved.getAvailable()).thenReturn(vehicle.getAvailable());
                    when(saved.getDepot()).thenReturn(depot);

                    return saved;
                });

        VehicleRequestDTO request =
                new VehicleRequestDTO("Test Truck", 100, true);

        var response = vehicleService.create(request);

        assertEquals("Test Truck", response.name());
        assertEquals(100, response.capacity());

        verify(vehicleRepository).save(any(Vehicle.class));
    }
}
