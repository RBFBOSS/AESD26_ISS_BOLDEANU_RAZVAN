package com.example.Lab3;

import org.springframework.stereotype.Component;

@Component
public class Battery {

    public Battery() {
        System.out.println("Battery bean created");
    }

    public void supplyPower() {
        System.out.println("Battery: Supplying electrical power.");
    }
}
