package com.example.lab2advanced.special;

import com.example.lab2advanced.annotation.MyComponent;

@MyComponent
public class StaticComponent {

    static {
        InitializationTracker.markInitialized();
    }

    public void execute() {
        System.out.println("StaticComponent executing.");
    }
}
