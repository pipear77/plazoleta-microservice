package com.pragma.plazoleta.api.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas de Swagger
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // Reglas de negocio HU05
                        .requestMatchers(HttpMethod.POST, "/api/v1/restaurants").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/v1/dishes").hasRole("PROPIETARIO")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/dishes/**").hasRole("PROPIETARIO")

                        // REGLA: HU07 Habilitar/Deshabilitar plato
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/dishes/**").hasRole("PROPIETARIO")

                        .anyRequest().authenticated()
                )
                // Agregamos nuestro filtro personalizado antes del filtro de validación tradicional
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}