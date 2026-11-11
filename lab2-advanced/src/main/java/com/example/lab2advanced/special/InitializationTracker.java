package com.example.lab2advanced.special;

public class InitializationTracker {

    private static boolean initialized = false;

    public static void markInitialized() {
        initialized = true;
        System.out.println(
                "[STATIC INITIALIZER] StaticComponent initialized!"
        );
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
