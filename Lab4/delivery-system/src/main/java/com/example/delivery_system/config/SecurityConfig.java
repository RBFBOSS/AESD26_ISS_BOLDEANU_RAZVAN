package com.example.delivery_system.config;

import com.example.delivery_system.repository.AppUserRepository;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Profile("!keycloak")
@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(
            AppUserRepository repository) {

        return email -> {
            var user = repository.findByEmail(email)
                    .orElseThrow(() ->
                            new org.springframework.security.core.userdetails
                                    .UsernameNotFoundException(
                                    "User not found"
                            ));

            return User.withUsername(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getRole().name())
                    .build();
        };
    }

    @Bean
    public SecretKey jwtSecretKey(
            @Value("${app.jwt.secret}") String secret) {

        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);

        if (bytes.length < 32) {
            throw new IllegalArgumentException(
                    "JWT secret must contain at least 32 bytes"
            );
        }

        return new SecretKeySpec(bytes, "HmacSHA256");
    }

    @Bean
    public JwtEncoder jwtEncoder(SecretKey secretKey) {
        return new NimbusJwtEncoder(
                new ImmutableSecret<>(secretKey)
        );
    }

    @Bean
    public JwtDecoder jwtDecoder(SecretKey secretKey) {

        NimbusJwtDecoder decoder = NimbusJwtDecoder
                .withSecretKey(secretKey)
                .macAlgorithm(
                        org.springframework.security.oauth2.jose.jws
                                .MacAlgorithm.HS256
                )
                .build();

        decoder.setJwtValidator(
                JwtValidators.createDefaultWithIssuer(
                        "delivery-system"
                )
        );

        return decoder;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> roles = jwt.getClaimAsStringList("roles");

            if (roles == null) {
                return List.of();
            }

            return roles.stream()
                    .map(role ->
                            (org.springframework.security.core.GrantedAuthority)
                                    new SimpleGrantedAuthority("ROLE_" + role))
                    .toList();
        });

        return converter;
    }

    @Configuration
    @Profile("prod")
    @EnableMethodSecurity
    public static class ProductionSecurity {

        @Bean
        public SecurityFilterChain securityFilterChain(
                HttpSecurity http,
                JwtAuthenticationConverter jwtConverter)
                throws Exception {

            http
                    .csrf(csrf -> csrf.disable())
                    .sessionManagement(session ->
                            session.sessionCreationPolicy(
                                    SessionCreationPolicy.STATELESS
                            ))
                    .authorizeHttpRequests(auth -> auth

                            .requestMatchers("/api/admin/**").hasRole("ADMIN")
                            .requestMatchers(
                                    "/api/auth/**",
                                    "/swagger-ui/**",
                                    "/swagger-ui.html",
                                    "/v3/api-docs/**",
                                    "/error"
                            ).permitAll()

                            .requestMatchers(
                                    HttpMethod.GET,
                                    "/api/**"
                            ).permitAll()

                            .requestMatchers(
                                    HttpMethod.POST,
                                    "/api/customers/**"
                            ).hasRole("ADMIN")

                            .requestMatchers(
                                    HttpMethod.PUT,
                                    "/api/customers/**"
                            ).hasRole("ADMIN")

                            .requestMatchers(
                                    HttpMethod.DELETE,
                                    "/api/customers/**"
                            ).hasRole("ADMIN")

                            .requestMatchers(
                                    HttpMethod.POST,
                                    "/api/vehicles/**",
                                    "/api/scenarios/**"
                            ).hasRole("ADMIN")

                            .requestMatchers(
                                    HttpMethod.PUT,
                                    "/api/vehicles/**",
                                    "/api/scenarios/**"
                            ).hasRole("ADMIN")

                            .requestMatchers(
                                    HttpMethod.DELETE,
                                    "/api/vehicles/**",
                                    "/api/scenarios/**"
                            ).hasRole("ADMIN")

                            .requestMatchers(
                                    HttpMethod.POST,
                                    "/api/deliveries/**"
                            ).hasAnyRole("ADMIN", "DRIVER")

                            .requestMatchers(
                                    HttpMethod.PUT,
                                    "/api/deliveries/**"
                            ).hasAnyRole("ADMIN", "DRIVER")

                            .requestMatchers(
                                    HttpMethod.DELETE,
                                    "/api/deliveries/**"
                            ).hasRole("ADMIN")

                            .requestMatchers(HttpMethod.POST, "/api/v1/customers/**", "/api/v2/customers/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/api/v1/customers/**", "/api/v2/customers/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/api/v1/customers/**", "/api/v2/customers/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.POST, "/api/v1/deliveries/**", "/api/v2/deliveries/**").hasAnyRole("ADMIN", "DRIVER")
                            .requestMatchers(HttpMethod.PUT, "/api/v1/deliveries/**", "/api/v2/deliveries/**").hasAnyRole("ADMIN", "DRIVER")
                            .requestMatchers(HttpMethod.DELETE, "/api/v1/deliveries/**", "/api/v2/deliveries/**").hasRole("ADMIN")
                            .anyRequest().authenticated()
                    )
                    .oauth2ResourceServer(oauth ->
                            oauth.jwt(jwt ->
                                    jwt.jwtAuthenticationConverter(
                                            jwtConverter
                                    )
                            )
                    );

            return http.build();
        }
    }
}
