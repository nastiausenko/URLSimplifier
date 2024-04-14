package com.linkurlshorter.urlshortener.security;

import com.linkurlshorter.urlshortener.jwt.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for Spring Security.
 *
 * <p>This class configures security settings such as authentication, authorization,
 * and session management for the application.
 *
 * @author Egor Sivenko
 * @see org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
 * @see org.springframework.context.annotation.Configuration
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Custom implementation of UserDetailsService for loading user details during authentication.
     */
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * Filter class for JWT authentication.
     */
    private final JwtRequestFilter jwtRequestFilter;

    /**
     * Configures the security filter chain for the application.
     *
     * @param http HttpSecurity object to configure security settings
     * @return SecurityFilterChain configured with specified security settings
     * @throws Exception if an error occurs while configuring security settings
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .userDetailsService(customUserDetailsService)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
