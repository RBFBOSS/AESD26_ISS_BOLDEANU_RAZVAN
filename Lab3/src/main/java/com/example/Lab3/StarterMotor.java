package com.example.Lab3;

import org.springframework.stereotype.Component;

@Component
public class StarterMotor {

    public StarterMotor() {
        System.out.println("StarterMotor bean created");
    }

    public void crankEngine() {
        System.out.println(
                "Starter motor: Turning the engine over."
        );
    }
}
