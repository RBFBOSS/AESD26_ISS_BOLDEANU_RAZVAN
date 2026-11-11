package com.example.library.policy;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class BorrowingPolicyConfiguration {
    @Bean
    @Primary
    public BorrowingPolicy borrowingPolicy(
            @Value("${library.borrowing-policy:drools}") String name,
            @Qualifier("fixed") BorrowingPolicy fixed,
            @Qualifier("roleBased") BorrowingPolicy roleBased,
            @Qualifier("drools") BorrowingPolicy drools) {
        return switch (name) {
            case "fixed" -> fixed;
            case "roleBased" -> roleBased;
            case "drools" -> drools;
            default -> throw new IllegalArgumentException("Unknown policy: " + name);
        };
    }
}
