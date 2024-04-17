package com.linkurlshorter.urlshortener.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkurlshorter.urlshortener.auth.dto.AuthRequest;
import com.linkurlshorter.urlshortener.jwt.JwtUtil;
import com.linkurlshorter.urlshortener.security.CustomUserDetailsService;
import com.linkurlshorter.urlshortener.security.SecurityUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
     * Test case for the {@link UserController#changePassword(ChangeUserPasswordRequest)} method.
     */
    @Test
    @WithMockUser
    void testChangePassword() throws Exception {
        given(jwtUtil.getEmailFromToken(any(String.class))).willReturn("test@example.com");
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
//    @Test
//    @WithMockUser
//    void changeEmailTest() throws Exception {
//        ChangeUserEmailRequest request = new ChangeUserEmailRequest("newEmail@khj.gf");
//        User user = User.builder()
//                .email(request.getNewEmail())
//                .build();
//        given(userService.findByEmail(anyString())).willReturn(user);
//        given(userService.updateByEmailDynamically(any(User.class), anyString())).willReturn(1);
//
//        ResultActions resultActions = mockMvc.perform(post("/api/V1/user/change-email")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)));
//
//        resultActions.andExpect(status().isOk());
//    }


//
//    /**
//     * Test case for the {@link UserController#changeEmail(ChangeUserEmailRequest)} method when the user with the
//     * provided email is not found.
//     */
//    @Test
//    void changeEmailFailedTest() throws Exception {
//        ChangeUserEmailRequest request = new ChangeUserEmailRequest("failed@email.com");
//        authentication = new UsernamePasswordAuthenticationToken("user@email.com", "PAssWORD1");
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        mockMvc.perform(post("/api/V1/user/change-email")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isNotFound());
//    }
}
