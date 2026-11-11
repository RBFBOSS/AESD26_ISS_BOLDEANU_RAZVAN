
package com.example.delivery_system.config;

import com.example.delivery_system.entity.AppUser;
import com.example.delivery_system.entity.Role;
import com.example.delivery_system.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Locale;

@Configuration
@Profile("prod")
public class AdminBootstrapConfig {

    @Bean
    public CommandLineRunner createInitialAdmin(
            AppUserRepository repository,
            PasswordEncoder encoder,
            @Value("${app.admin.email:}") String email,
            @Value("${app.admin.password:}") String password) {

        return args -> {

            if (email.isBlank() || password.isBlank()) {
                return;
            }

            String normalizedEmail = email.trim()
                    .toLowerCase(Locale.ROOT);

            if (password.length() < 12) {
                throw new IllegalArgumentException(
                        "Initial admin password must have at least 12 characters"
                );
            }

            if (repository.existsByEmail(normalizedEmail)) {
                return;
            }

            repository.save(new AppUser(
                    normalizedEmail,
                    encoder.encode(password),
                    Role.ADMIN
            ));
        };
    }
}
