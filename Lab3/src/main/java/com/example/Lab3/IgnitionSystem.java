package com.example.Lab3;

import org.springframework.stereotype.Component;

@Component
public class IgnitionSystem {

    public IgnitionSystem() {
        System.out.println("IgnitionSystem bean created");
    }

    public void ignite() {
        System.out.println("Ignition system: Igniting the fuel-air mixture.");
    }
}
