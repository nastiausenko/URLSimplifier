package com.linkurlshorter.urlshortener.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkurlshorter.urlshortener.jwt.JwtUtil;
import com.linkurlshorter.urlshortener.security.CustomUserDetailsService;
import com.linkurlshorter.urlshortener.security.SecurityUserDetails;
import com.linkurlshorter.urlshortener.user.request.ChangeUserEmailRequest;
import com.linkurlshorter.urlshortener.user.request.ChangeUserPasswordRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@link UserController} class.
 *
 * @author Anastasiia Usenko
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private User user;

    /**
     * Set up method to initialize test data before each test method.
     */
    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(UUID.fromString("84991c79-f6a9-4b7b-b1b4-0d66c0b92c81"))
                .email("test1@gmail.com")
                .password("Password1")
                .role(UserRole.USER)
                .build();
    }

    /**
     * Test case for the {@link UserController#changePassword(ChangeUserPasswordRequest)} method.
     */
    @Test
    @WithMockUser
    void testChangePassword() throws Exception {
        given(jwtUtil.getEmailFromToken(any(String.class))).willReturn(user.getEmail());
        given(userService.updateByEmailDynamically(any(User.class), any(String.class))).willReturn(1);

        ChangeUserPasswordRequest request = new ChangeUserPasswordRequest("newPassword1");

        ResultActions resultActions = mockMvc.perform(post("/api/V1/user/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("ok"));
    }

    /**
     * Test case for the {@link UserController#changePassword(ChangeUserPasswordRequest)} method when the user with the 
     * provided email is not found.
     */
    @Test
    @WithMockUser
    void changePasswordFailedTest() throws Exception {
        ChangeUserPasswordRequest request = new ChangeUserPasswordRequest("newPassword1");
        given(jwtUtil.getEmailFromToken(any(String.class))).willReturn("failed@email.com");
        given(userService.updateByEmailDynamically(any(User.class), any(String.class))).willReturn(0);

        ResultActions resultActions = mockMvc.perform(post("/api/V1/user/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        resultActions.andExpect(status().isNotFound());
    }

    /**
     * Test case for the {@link UserController#changeEmail(ChangeUserEmailRequest)} method.
     */
    @Test
    @WithMockUser
    void changeEmailTest() throws Exception {
        given(jwtUtil.getEmailFromToken(any(String.class))).willReturn(user.getEmail());
        given(userService.updateByEmailDynamically(any(User.class), anyString())).willReturn(1);

        ChangeUserEmailRequest request = new ChangeUserEmailRequest("newEmail@example.com");
        UserDetails userDetails = new SecurityUserDetails(user);
        given(customUserDetailsService.loadUserByUsername(anyString())).willReturn(userDetails);

        ResultActions resultActions = mockMvc.perform(post("/api/V1/user/change-email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("ok"));
    }

    /**
     * Test case for the {@link UserController#changeEmail(ChangeUserEmailRequest)} method when the user with the
     * provided email is not found.
     */
    @Test
    @WithMockUser
    void changeEmailFailedTest() throws Exception {
        given(jwtUtil.getEmailFromToken(any(String.class))).willReturn("nonExistent@email.com");
        given(userService.updateByEmailDynamically(any(User.class), anyString())).willReturn(0);

        ChangeUserEmailRequest request = new ChangeUserEmailRequest("failed@email.com");
        UserDetails userDetails = new SecurityUserDetails(user);
        given(customUserDetailsService.loadUserByUsername(anyString())).willReturn(userDetails);

        mockMvc.perform(post("/api/V1/user/change-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }


    /**
     * Test case for the {@link UserController#changeEmail(ChangeUserEmailRequest)} method when the user with the
     * provided email is already taken.
     */
    @Test
    @WithMockUser
    void changeEmailAlreadyTakenTest() throws Exception {
        String newEmail = "existing@example.com";
        ChangeUserEmailRequest request = new ChangeUserEmailRequest();
        request.setNewEmail(newEmail);
        when(userService.existsByEmail(newEmail)).thenReturn(true);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/api/V1/user/change-email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // Assert
        resultActions.andExpect(status().isBadRequest());
    }
}
