package com.example.library.policy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class BorrowingPolicyConfiguration {

    @Bean
    public BorrowingPolicy borrowingPolicy(
            @Value("${library.borrowing-policy:fixed}")
            String policyName,
            Map<String, BorrowingPolicy> policies) {

        BorrowingPolicy selected = policies.get(policyName);

        if (selected == null) {
            throw new IllegalStateException(
                    "Unknown borrowing policy: " + policyName
                            + ". Use 'fixed' or 'roleBased'.");
        }

        return selected;
    }
}