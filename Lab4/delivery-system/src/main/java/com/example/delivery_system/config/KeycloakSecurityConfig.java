
package com.example.delivery_system.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Configuration
@Profile("keycloak")
@EnableMethodSecurity
public class KeycloakSecurityConfig {

    // Converts Keycloak realm roles into Spring Security roles.
    private Collection<GrantedAuthority> mapRoles(Object realmAccess) {

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (!(realmAccess instanceof Map<?, ?> access)) {
            return authorities;
        }

        Object rolesObject = access.get("roles");

        if (!(rolesObject instanceof Collection<?> roles)) {
            return authorities;
        }

        for (Object roleObject : roles) {

            String role = String.valueOf(roleObject)
                    .toUpperCase(Locale.ROOT);

            if (role.equals("ADMIN")
                    || role.equals("CUSTOMER")
                    || role.equals("DRIVER")) {

                authorities.add(
                        new SimpleGrantedAuthority("ROLE_" + role)
                );
            }
        }

        return authorities;
    }

    // Maps roles from Keycloak JWT access tokens.
    @Bean
    public JwtAuthenticationConverter keycloakJwtConverter() {

        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(
                jwt -> mapRoles(jwt.getClaim("realm_access"))
        );

        return converter;
    }

    // Maps roles from Keycloak OIDC browser login.
    @Bean
    public GrantedAuthoritiesMapper keycloakAuthoritiesMapper() {

        return originalAuthorities -> {

            List<GrantedAuthority> mapped =
                    new ArrayList<>(originalAuthorities);

            for (GrantedAuthority authority : originalAuthorities) {

                if (authority instanceof OidcUserAuthority oidc) {

                    Map<String, Object> claims =
                            oidc.getIdToken().getClaims();

                    mapped.addAll(
                            mapRoles(claims.get("realm_access"))
                    );

                    // Some Keycloak configurations expose
                    // realm roles through UserInfo instead.
                    if (oidc.getUserInfo() != null) {
                        mapped.addAll(
                                mapRoles(
                                        oidc.getUserInfo()
                                                .getClaim("realm_access")
                                )
                        );
                    }
                }
            }

            return mapped;
        };
    }

    @Bean
    public SecurityFilterChain keycloakFilterChain(
            HttpSecurity http,

            @Qualifier("keycloakJwtConverter")
            JwtAuthenticationConverter keycloakJwtConverter,

            GrantedAuthoritiesMapper keycloakAuthoritiesMapper)
            throws Exception {

        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")
                )

                .authorizeHttpRequests(auth -> auth

                        // Public Keycloak login and Swagger endpoints
                        .requestMatchers(
                                "/oauth2/**",
                                "/login/**",
                                "/error",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Local authentication is disabled
                        // when using Keycloak.
                        .requestMatchers("/api/auth/**")
                        .denyAll()

                        // Administrator endpoints
                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")

                        // Public GET endpoints
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/**"
                        ).permitAll()

                        // Only ADMIN can manage customers,
                        // vehicles and scenarios.
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/customers/**",
                                "/api/vehicles/**",
                                "/api/scenarios/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/customers/**",
                                "/api/vehicles/**",
                                "/api/scenarios/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/customers/**",
                                "/api/vehicles/**",
                                "/api/scenarios/**",
                                "/api/deliveries/**"
                        ).hasRole("ADMIN")

                        // ADMIN and DRIVER can create deliveries.
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/deliveries/**"
                        ).hasAnyRole("ADMIN", "DRIVER")

                        // ADMIN and DRIVER can update deliveries.
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/deliveries/**"
                        ).hasAnyRole("ADMIN", "DRIVER")

                            .requestMatchers(HttpMethod.POST, "/api/v1/customers/**", "/api/v2/customers/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/api/v1/customers/**", "/api/v2/customers/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/api/v1/customers/**", "/api/v2/customers/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.POST, "/api/v1/deliveries/**", "/api/v2/deliveries/**").hasAnyRole("ADMIN", "DRIVER")
                            .requestMatchers(HttpMethod.PUT, "/api/v1/deliveries/**", "/api/v2/deliveries/**").hasAnyRole("ADMIN", "DRIVER")
                            .requestMatchers(HttpMethod.DELETE, "/api/v1/deliveries/**", "/api/v2/deliveries/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                // Browser authentication through Keycloak.
                .oauth2Login(oauth ->
                        oauth.userInfoEndpoint(userInfo ->
                                userInfo.userAuthoritiesMapper(
                                        keycloakAuthoritiesMapper
                                )
                        )
                )

                // API authentication using Keycloak JWT tokens.
                .oauth2ResourceServer(oauth ->
                        oauth.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(
                                        keycloakJwtConverter
                                )
                        )
                );

        return http.build();
    }
}
