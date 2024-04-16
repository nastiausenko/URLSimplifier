package com.linkurlshorter.urlshortener.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;


import static org.assertj.core.api.Assertions.assertThat;


class JwtUtilTest {
    private JwtUtil jwtUtil;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        authentication = new UsernamePasswordAuthenticationToken("test@example.com", null);
    }

    @Test
    void generateTokenTest() {
        String token = jwtUtil.generateToken(authentication);
        assertThat(token).isNotNull();
    }

    @Test
    void getEmailFromTokenTest() {
        String email = authentication.getName();
        String token = jwtUtil.generateToken(authentication);

        String extractedEmail = jwtUtil.getEmailFromToken(token);
        assertThat(extractedEmail).isNotNull().isEqualTo(email);
    }
}
