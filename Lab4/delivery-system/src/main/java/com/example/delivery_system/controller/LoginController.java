package com.example.delivery_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class LoginController {

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login() {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Mock login endpoint reached successfully",
                        "status", "success"
                )
        );
    }
}