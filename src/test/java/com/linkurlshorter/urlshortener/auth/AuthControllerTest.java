package com.linkurlshorter.urlshortener.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.exception.UnauthorizedException;
import com.linkurlshorter.urlshortener.auth.dto.AuthRequest;
import com.linkurlshorter.urlshortener.auth.exception.EmailAlreadyTakenException;
import com.linkurlshorter.urlshortener.TestConfig;
import com.linkurlshorter.urlshortener.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for {@link AuthController} class.
 *
 * @author Anastasiia Usenko
 */
@WebMvcTest(controllers = AuthController.class)
@Import({SecurityConfig.class, TestConfig.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    /**
     * Test case for the {@link AuthController#register(AuthRequest)} method.
     */
    @Test
    void registrationSuccessfulTest() throws Exception {
        AuthRequest request = new AuthRequest("test@email.com", "Password1");
        when(authService.registerUser(request)).thenReturn(String.valueOf(request));

        ResultActions resultActions = mockMvc.perform(post("/api/V1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        resultActions.andExpect(jsonPath("$.message").value("User registered successfully!"))
                .andExpect(jsonPath("$.jwtToken").exists());
    }

    /**
     * Test case for the {@link AuthController#register(AuthRequest)} method when the user with the
     * provided email already exists.
     */
    @Test
    void registrationFailedTest() throws Exception {
        AuthRequest request = new AuthRequest("test1@email.com", "Password1");
        when(authService.registerUser(request)).thenThrow(EmailAlreadyTakenException.class);

        ResultActions resultActions = mockMvc.perform(post("/api/V1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        resultActions.andExpect(status().isBadRequest());
    }

    /**
     * Test case for the {@link AuthController#login(AuthRequest)} method.
     */
    @Test
    void loginSuccessfulTest() throws Exception {
        AuthRequest request = new AuthRequest("test2@email.com", "Password1");
        when(authService.loginUser(request)).thenReturn(String.valueOf(request));

        ResultActions resultActions = mockMvc.perform(post("/api/V1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User logged in successfully!"))
                .andExpect(jsonPath("$.jwtToken").exists());
    }

    /**
     * Test case for the {@link AuthController#login(AuthRequest)} method when the user is not registered.
     */
    @Test
    void loginFailedTest() throws Exception {
        AuthRequest request = new AuthRequest("test3@email.com", "Password1");
        when(authService.loginUser(request)).thenThrow(UsernameNotFoundException.class);

        ResultActions resultActions = mockMvc.perform(post("/api/V1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        resultActions.andExpect(status().isUnauthorized());
    }
}
