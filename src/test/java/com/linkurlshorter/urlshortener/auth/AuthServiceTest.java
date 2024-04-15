package com.linkurlshorter.urlshortener.auth;

import com.linkurlshorter.urlshortener.auth.dto.AuthRequest;
import com.linkurlshorter.urlshortener.auth.exception.EmailAlreadyTakenException;
import com.linkurlshorter.urlshortener.jwt.JwtUtil;
import com.linkurlshorter.urlshortener.user.User;
import com.linkurlshorter.urlshortener.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void registrationSuccessfulTest() {
        AuthRequest authRequest = new AuthRequest("test@example.com", "password");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("ENCRYPTED_PASSWORD");
        when(jwtUtil.generateToken(any())).thenReturn("JWT");

        String token = authService.registerUser(authRequest);

        assertThat(token).isNotNull()
                .isEqualTo("JWT");
    }

    @Test
    void registrationFailedTest() {
        AuthRequest authRequest = new AuthRequest("test@example.com", "password");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> authService.registerUser(authRequest))
                .isInstanceOf(EmailAlreadyTakenException.class);
    }

    @Test
    void loginSuccessfulTest() {
        AuthRequest authRequest = new AuthRequest("test@example.com", "password");
        Authentication authentication = new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword());
        String expectedToken = "SAMPLE_JWT";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtil.generateToken(authentication)).thenReturn(expectedToken);

        String actualToken = authService.loginUser(authRequest);

        assertThat(expectedToken).isEqualTo(actualToken);
    }
}
