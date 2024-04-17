package com.linkurlshorter.urlshortener.jwt;

import com.linkurlshorter.urlshortener.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtRequestFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    @Test
    void doFilterInternal_ValidToken_ShouldAuthenticateUser() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        String token = "Bearer valid_token";
        String email = "test@example.com";
        UserDetails userDetails = User.withUsername(email).password("Password1").roles("USER").build();

        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtil.getEmailFromToken(token.substring(7))).thenReturn(email);
        when(customUserDetailsService.loadUserByUsername(email)).thenReturn(userDetails);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        verify(customUserDetailsService, times(1)).loadUserByUsername(email);
        verify(filterChain, times(1)).doFilter(any(), any());
    }

    @Test
    void doFilterInternal_InvalidToken_ShouldNotAuthenticateUser() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        String token = "Bearer invalid_token";

        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtil.getEmailFromToken(token.substring(7))).thenReturn(null);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        verify(customUserDetailsService, never()).loadUserByUsername(any());
        verify(filterChain, times(1)).doFilter(any(), any());
    }
}
