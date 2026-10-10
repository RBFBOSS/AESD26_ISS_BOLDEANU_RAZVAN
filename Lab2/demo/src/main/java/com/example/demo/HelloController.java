package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Value("${app.greeting:Hello Default!}")
    private String greetingMessage;

    @GetMapping("/hello")
    public String sayHello() {
        return greetingMessage;
    }
}