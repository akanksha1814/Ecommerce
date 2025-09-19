package com.example.ecommerce_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // new syntax to disable CSRF
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/**").permitAll() // allow all endpoints
            );

        return http.build();
    }
}
