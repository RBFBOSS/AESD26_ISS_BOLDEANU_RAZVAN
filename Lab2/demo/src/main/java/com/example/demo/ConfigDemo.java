package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConfigDemo implements CommandLineRunner {

    @Value("${app.name}")
    private String appName;

    @Value("${app.environment}")
    private String environment;

    @Value("${app.message}")
    private String message;

    @Override
    public void run(String... args) {
        System.out.println("Application: " + appName);
        System.out.println("Environment: " + environment);
        System.out.println("Message: " + message);
    }
}
