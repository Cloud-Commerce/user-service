package edu.ecom.user.security.config;

import edu.ecom.user.filter.HmacSignatureFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class SecurityConfig {

  // 1. Internal API Security (Higher Priority)
  @Bean
  @Order(1)
  public SecurityFilterChain internalApiFilterChain(HttpSecurity http, HmacSignatureFilter signatureFilter) throws Exception {
    http
        // 1. FIRST: Identify if this chain should process the request
        .securityMatcher("/api/internal/**")
        // 2. THEN: Apply rules to matched requests
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/internal/**").permitAll() // All need auth
        )
        .csrf(AbstractHttpConfigurer::disable)
        .addFilterBefore(signatureFilter, AuthorizationFilter.class);

    return http.build();
  }

  // 2. Public API Security
  @Bean
  @Order(2)
  public SecurityFilterChain publicApiFilterChain(HttpSecurity http) throws Exception {
    http
        .securityMatcher("/users/**")
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/users/public/**").permitAll()
            .anyRequest().authenticated()
        )
        .csrf(AbstractHttpConfigurer::disable);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    // BCrypt password encoding with strength 12
    return new BCryptPasswordEncoder(12);
  }
}