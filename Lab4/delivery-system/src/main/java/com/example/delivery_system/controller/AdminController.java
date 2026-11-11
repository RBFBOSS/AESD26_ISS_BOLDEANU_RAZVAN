
package com.example.delivery_system.controller;

import com.example.delivery_system.dto.CreateDriverAccountDTO;
import com.example.delivery_system.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/drivers")
    public ResponseEntity<Map<String, Long>> createDriver(
            @Valid @RequestBody CreateDriverAccountDTO dto) {

        Long id = adminService.createDriver(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("driverId", id));
    }
}
