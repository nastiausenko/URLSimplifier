package com.linkurlshorter.urlshortener.link;

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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the LinkController class.
 *
 * @author Ivan Shalaiev
 * @version 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@Testcontainers
@Transactional
@Rollback
class LinkControllerIntegrationTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.0-alpine");

    @Autowired
    private MockMvc mockMvc;
    private final String baseUrl = "/api/V1/link/";
    private String token;
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

    @ParameterizedTest
    @ValueSource(strings = {"https://www.youtube.com",
            "https://open.spotify.com/",
            "https://www.google.com",
            "https://www.facebook.com"})
    void createShortLinkWorksCorrectly(String url) throws Exception {
        CreateLinkRequest createLinkRequest = new CreateLinkRequest(url);
        mockMvc.perform(post(baseUrl + "create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(createLinkRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("ok"))
                .andExpect(jsonPath("$.shortLink").isNotEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"https://www.",
            "https://open.spotifycom",
            "https://www.google.com@",
            "https://www.facebook.com%"})
    void createShortLinkFailsWhenUrlIsInvalid(String url) throws Exception {
        CreateLinkRequest createLinkRequest = new CreateLinkRequest(url);
        mockMvc.perform(post(baseUrl + "create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(createLinkRequest)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed!"))
                .andExpect(jsonPath("$.exceptionMessage").value("Not valid format url!"));
    }

    @Test
    void deleteLinkWorksCorrectly() throws Exception {
        UUID id = UUID.fromString("3053e49b-6da3-4389-9d06-23b2d57b6f25");
        mockMvc.perform(post(baseUrl + "delete" + "?id=" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("ok"));
    }
    @Test
    void deleteLinkFailsWhenIdIsInvalid() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(post(baseUrl + "delete" + "?id=" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("No link by provided id found"))
                .andExpect(jsonPath("$.exceptionMessage").value("No link by provided id found"));
    }
    @Test
    void deleteLinkFailsWhenIdIsNull() throws Exception {
        mockMvc.perform(post(baseUrl + "delete" + "?id=" + null)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().isInternalServerError());
    }
    @Test
    void deleteLinkFailsWhenUserHasNoRightsForThisLink() throws Exception {
        authRequest = new AuthRequest("user-test@example.com", "Pass1234");
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/V1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)));
        MvcResult mvcResult = result.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(contentAsString);
        this.token = "Bearer " + jsonObject.getString("jwtToken");
        UUID id = UUID.fromString("3053e49b-6da3-4389-9d06-23b2d57b6f25");
        mockMvc.perform(post(baseUrl + "delete" + "?id=" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.statusCode").value(403))
                .andExpect(jsonPath("$.message").value("Operation forbidden!"))
                .andExpect(jsonPath("$.exceptionMessage").value("Operation forbidden!"));
    }
}
