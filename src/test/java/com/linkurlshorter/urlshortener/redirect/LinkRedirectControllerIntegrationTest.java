package com.linkurlshorter.urlshortener.redirect;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the LinkRedirectController class.
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
public class LinkRedirectControllerIntegrationTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.0-alpine");
    @Container
    @ServiceConnection
    static GenericContainer<?> redis = new GenericContainer<>("redis:latest").withExposedPorts(6379);
    @Autowired
    private MockMvc mockMvc;

    @Test
    void redirectToOriginalLinkWoksCorrectly() throws Exception {
        String originalLink = "https://www.youtube.com";
        String shortLink = "short-link-1";
        mockMvc.perform(get("/" + shortLink))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(originalLink));
    }
    @Test
    void redirectToOriginalLinkFailedWithInvalidLink() throws Exception {
        String shortLink = "invalid-link";
        mockMvc.perform(get("/" + shortLink))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("No link by provided short link found"))
        .andExpect(jsonPath("$.path").value("/" + shortLink));
    }
    @Test
    void redirectToOriginalLinkFailedWithNull() throws Exception {
        String shortLink = null;
        mockMvc.perform(get("/" + shortLink))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("No link by provided short link found"))
                .andExpect(jsonPath("$.path").value("/" + shortLink));
    }
}
