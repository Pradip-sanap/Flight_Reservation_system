package com.flight.config;

import com.flight.jwt.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JwtFilter jwtFilter;


    @Bean
    public SecurityFilterChain chain(HttpSecurity http){

        // Custom entry point to handle 401 Unauthorized
        AuthenticationEntryPoint unauthorizedHandler = (request, response, authException) -> {
            log.warn("Unauthorized request to: {}", request.getServletPath());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized: " + authException.getMessage() + "\"}");
        };

        log.info("Request passing through Security Filter chain");
        log.debug("Initializing Security Filter Chain...");
        try {
            SecurityFilterChain chain = http
                    .csrf(csrf -> csrf.disable())                 // Disable Cross-Site Request Forgery
                    // .cors(cors -> cors.disable())                                    // Uncomment if CORS not needed
                    .sessionManagement(session ->  session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/auth/**").permitAll()                   // Auth endpoints open
                            .requestMatchers(HttpMethod.POST, "/api/user").permitAll() // Registration endpoint open
                            .anyRequest()
                            .authenticated()                                                    // All other endpoints require auth
                    )
                    .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler)) // Handle 401
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();

            log.info("Security Filter Chain initialized successfully.");

            return chain;

        } catch (Exception ex) {
            log.error("Error initializing Security Filter Chain", ex);
            throw new RuntimeException("Failed to initialize security configuration", ex);
        }

    }
}
