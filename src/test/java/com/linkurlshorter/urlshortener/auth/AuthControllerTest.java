package com.linkurlshorter.urlshortener.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkurlshorter.urlshortener.auth.dto.AuthRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Unit tests for {@link AuthController} class.
 *
 * @author Anastasiia Usenko
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Testcontainers
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.0-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test case for the {@link AuthController#register(AuthRequest)} method.
     */
    @Test
    void registrationSuccessfulTest() throws Exception {
        AuthRequest request = new AuthRequest("test@email.com", "Password1");
        performRegistration(request)
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User registered successfully!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.jwtToken").exists());
    }

    /**
     * Test case for the {@link AuthController#register(AuthRequest)} method when the user with the
     * provided email already exists.
     */
    @Test
    void registrationFailedTest() throws Exception {
        AuthRequest request = new AuthRequest("test1@email.com", "Password1");
        performRegistration(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User registered successfully!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.jwtToken").exists());

        performRegistration(request).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Test case for the {@link AuthController#login(AuthRequest)} method.
     */
    @Test
    void loginSuccessfulTest() throws Exception {
        AuthRequest request = new AuthRequest("test2@email.com", "Password1");
        performRegistration(request).andExpect(MockMvcResultMatchers.status().isOk());

        performLogin(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User logged in successfully!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.jwtToken").exists());
    }

    /**
     * Test case for the {@link AuthController#login(AuthRequest)} method when the user is not registered.
     */
    @Test
    void loginFailedTest() throws Exception {
        AuthRequest request = new AuthRequest("test3@email.com", "Password1");
        performLogin(request).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    /**
     * Performs registration request.
     *
     * @param request the registration request
     * @return the result actions after performing registration
     * @throws Exception if an error occurs during the registration process
     */
    private ResultActions performRegistration(AuthRequest request) throws Exception {
        return mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    /**
     * Performs login request.
     *
     * @param request the login request
     * @return the result actions after performing login
     * @throws Exception if an error occurs during the login process
     */
    private ResultActions performLogin(AuthRequest request) throws Exception {
        return mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }
}