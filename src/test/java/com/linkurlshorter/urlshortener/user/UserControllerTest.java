package com.linkurlshorter.urlshortener.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkurlshorter.urlshortener.auth.AuthController;
import com.linkurlshorter.urlshortener.auth.dto.AuthRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for {@link UserController} class.
 *
 * @author Anastasiia Usenko
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Testcontainers
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.0-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthController authController;
    private AuthRequest authRequest;
    private Authentication authentication;

    /**
     * Test case for the {@link UserController#changePassword(ChangeUserPasswordRequest)} method.
     */
    @Test
    void changePasswordTest() throws Exception {
        ChangeUserPasswordRequest request = new ChangeUserPasswordRequest("newPassword1");
        authRequest = new AuthRequest("test@email.com", "Password1");
        authentication = new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        authController.register(authRequest);

        mockMvc.perform(post("/api/V1/user/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.error").value("ok"));
    }

    /**
     * Test case for the {@link UserController#changePassword(ChangeUserPasswordRequest)} method when the user with the 
     * provided email is not found.
     */
    @Test
    void changePasswordFailedTest() throws Exception {
        ChangeUserPasswordRequest request = new ChangeUserPasswordRequest("newPassword1");
        authentication = new UsernamePasswordAuthenticationToken("failed@email.com", "PAssWORD1");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        mockMvc.perform(post("/api/V1/user/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    /**
     * Test case for the {@link UserController#changeEmail(ChangeUserEmailRequest)} method.
     */
    @Test
    void changeEmailTest() throws Exception {
        ChangeUserEmailRequest request = new ChangeUserEmailRequest("success@email.com");
        authRequest = new AuthRequest("test1@email.com", "Password1");
        authentication = new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        authController.register(authRequest);

        mockMvc.perform(post("/api/V1/user/change-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.error").value("ok"));
    }

    /**
     * Test case for the {@link UserController#changeEmail(ChangeUserEmailRequest)} method when the user with the 
     * provided email is not found.
     */
    @Test
    void changeEmailFailedTest() throws Exception {
        ChangeUserEmailRequest request = new ChangeUserEmailRequest("failed@email.com");
        authentication = new UsernamePasswordAuthenticationToken("user@email.com", "PAssWORD1");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        mockMvc.perform(post("/api/V1/user/change-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
