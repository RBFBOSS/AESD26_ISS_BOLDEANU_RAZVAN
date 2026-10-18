package com.example.Lab3;

import org.springframework.stereotype.Component;

@Component
public class FuelSystem {

    public FuelSystem() {
        System.out.println("FuelSystem bean created");
    }

    public void supplyFuel() {
        System.out.println("Fuel system: Supplying fuel to the engine.");
    }
}
