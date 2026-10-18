package com.example.Lab3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CarStarter {

    private final Battery battery;

    @Autowired
    private FuelSystem fuelSystem;

    private IgnitionSystem ignitionSystem;
    private boolean fieldInjected = false;

    public CarStarter(Battery battery) {
        this.battery = battery;

        System.out.println(
                "Constructor injection: Battery received."
        );
    }

    @Autowired
    public void setIgnitionSystem(IgnitionSystem ignitionSystem) {
        this.ignitionSystem = ignitionSystem;

        fieldInjected = (fuelSystem != null);

        System.out.println(
                "Setter injection: IgnitionSystem received."
        );
        System.out.println(
                "Field already injected: " + fieldInjected
        );
    }

    private StarterMotor starterMotor;

    @Autowired
    public void configureStarter(StarterMotor starterMotor) {
        this.starterMotor = starterMotor;

        System.out.println(
                "Method injection: StarterMotor received."
        );
        System.out.println(
                "Field already injected: " + (fuelSystem != null)
        );
    }

    public void startCar() {
        System.out.println("\nSTARTING CAR");

        battery.supplyPower();
        fuelSystem.supplyFuel();
        starterMotor.crankEngine();
        ignitionSystem.ignite();

        System.out.println("Engine started successfully!");
    }
}
