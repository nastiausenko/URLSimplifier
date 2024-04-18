package com.linkurlshorter.urlshortener.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkurlshorter.urlshortener.auth.dto.AuthRequest;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@Testcontainers
class UserControllerIntegrationTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.0-alpine");

    @Autowired
    private MockMvc mockMvc;
    private String token;
    private final String baseUrl = "/api/V1/user/";
    private AuthRequest authRequest;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() throws Exception {
        authRequest = new AuthRequest("user1@example.com", "Pass1234");
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/V1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)));
        MvcResult mvcResult = result.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(contentAsString);
        this.token = "Bearer " + jsonObject.getString("jwtToken");
    }

    @Test
    void changePasswordTest() throws Exception {
        authRequest = new AuthRequest("change-password-test@email.com", "Password1");
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/V1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)));
        MvcResult mvcResult = result.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(contentAsString);
        this.token = "Bearer " + jsonObject.getString("jwtToken");

        ChangeUserPasswordRequest request = new ChangeUserPasswordRequest("newPassword1");
        mockMvc.perform(post(baseUrl + "change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("ok"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "Password", "Pass123", "pass1234", "Pass 1234"})
    void changePasswordFailedWhenInvalidPasswordGivenTest(String password) throws Exception {
        ChangeUserPasswordRequest request = new ChangeUserPasswordRequest(password);
        mockMvc.perform(post(baseUrl + "change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void changeEmailTest() throws Exception {
        authRequest = new AuthRequest("change-email-test@email.com", "Password1");
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/V1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)));
        MvcResult mvcResult = result.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(contentAsString);
        this.token = "Bearer " + jsonObject.getString("jwtToken");

        ChangeUserEmailRequest request = new ChangeUserEmailRequest("success@email.com");
        mockMvc.perform(post(baseUrl + "change-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("ok"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "change email-test@email.com",
            " change-email-test@email.com",
            "change-email-test%@email.com",
            "change-email-test#@email.com",
            "change-email-test.email.com"})
//    TODO: add more email to test "change-email-test@email"
    void changeEmailFailedWhenInvalidEmailGivenTest(String email) throws Exception {
        ChangeUserEmailRequest request = new ChangeUserEmailRequest(email);
        mockMvc.perform(post(baseUrl + "change-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }
}
