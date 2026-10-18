package com.example.Lab3;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Lab3Application {

    public static void main(String[] args) {
        SpringApplication.run(Lab3Application.class, args);
    }

    @Bean
    CommandLineRunner run(CarStarter carStarter) {
        return args -> carStarter.startCar();
    }
}
