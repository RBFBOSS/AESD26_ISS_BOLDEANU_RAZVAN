
package com.example.delivery_system.service;

import com.example.delivery_system.dto.CreateDriverAccountDTO;
import com.example.delivery_system.entity.AppUser;
import com.example.delivery_system.entity.Driver;
import com.example.delivery_system.entity.Role;
import com.example.delivery_system.repository.AppUserRepository;
import com.example.delivery_system.repository.DriverRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
public class AdminService {

    private final AppUserRepository userRepository;
    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(
            AppUserRepository userRepository,
            DriverRepository driverRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Long createDriver(CreateDriverAccountDTO dto) {

        String email = dto.email()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (userRepository.existsByEmail(email)
                || driverRepository.findByEmail(email).isPresent()) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already exists"
            );
        }

        AppUser user = userRepository.save(
                new AppUser(
                        email,
                        passwordEncoder.encode(dto.password()),
                        Role.DRIVER
                )
        );

        Driver driver = new Driver(
                dto.name(),
                email,
                dto.phone()
        );

        driver.setUser(user);

        return driverRepository.save(driver).getId();
    }
}
