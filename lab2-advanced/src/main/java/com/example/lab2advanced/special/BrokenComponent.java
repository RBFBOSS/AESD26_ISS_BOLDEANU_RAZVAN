package com.example.lab2advanced.special;

import com.example.lab2advanced.annotation.MyComponent;

@MyComponent
public class BrokenComponent extends MissingDependency {

    public void execute() {
        System.out.println("BrokenComponent executing.");
    }
}
