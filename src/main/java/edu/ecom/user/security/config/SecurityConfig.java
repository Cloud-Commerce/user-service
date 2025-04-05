package edu.ecom.user.security.config;

import edu.ecom.user.filter.HmacSignatureFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

@Configuration
public class SecurityConfig {

  // 1. Internal API Security (Higher Priority)
  @Bean
  @Order(1)
  public SecurityFilterChain internalApiFilterChain(HttpSecurity http, HmacSignatureFilter signatureFilter) throws Exception {
    http
        // 1. FIRST: Identify if this chain should process the request
        .securityMatcher("/internal/**")
        // 2. THEN: Apply rules to matched requests
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/internal/**") // All need auth
            .access((authentication, context) -> {
              HttpServletRequest request = context.getRequest();
              if (new IpAddressMatcher("auth-service").matches(request)) {
                return new AuthorizationDecision(true);
              }
              return new AuthorizationDecision(false);
            })
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