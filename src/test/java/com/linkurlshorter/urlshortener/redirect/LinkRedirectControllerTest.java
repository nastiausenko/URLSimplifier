package com.linkurlshorter.urlshortener.redirect;

import com.linkurlshorter.urlshortener.TestConfig;
import com.linkurlshorter.urlshortener.link.*;
import com.linkurlshorter.urlshortener.security.SecurityConfig;
import com.linkurlshorter.urlshortener.user.User;
import com.linkurlshorter.urlshortener.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@link LinkRedirectController} class.
 *
 * @author Anastasiia Usenko
 */
@WebMvcTest(controllers = LinkRedirectController.class)
@Import({SecurityConfig.class, TestConfig.class})
class LinkRedirectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JedisPool jedisPool;

    @MockBean
    private Jedis jedis;

    @MockBean
    private LinkService linkService;

    private Link link;

    /**
     * Set up method to initialize test data before each test method.
     */
    @BeforeEach
    void setUp() {
        link = Link.builder()
                .id(UUID.fromString("3053e49b-6da3-4389-9d06-23b2d57b6f25"))
                .longLink("https://www.youtube.com")
                .shortLink("short-link-1")
                .user(User.builder()
                        .id(UUID.fromString("84991c79-f6a9-4b7b-b1b4-0d66c0b92c81"))
                        .email("user1@example.com")
                        .password("$2a$12$7Cp4On1DBNyCkz4TaZYc3O.A.CBKi4WXgXnlI4SD0yn7CgBX5Gd6O")
                        .role(UserRole.USER)
                        .build())
                .createdTime(LocalDateTime.of(2024, 4, 13, 10, 0))
                .expirationTime(LocalDateTime.of(2024, 5, 16, 8, 0))
                .statistics(100)
                .status(LinkStatus.ACTIVE)
                .build();
    }

    /**
     * Test case for the {@link LinkRedirectController#redirectToOriginalLink(String)} method
     * when there is a short link in the cache.
     */
    @Test
    void redirectToOriginalLinkInLinkCacheTest() throws Exception {
        when(jedisPool.getResource()).thenReturn(jedis);
        when(linkService.getLongLinkFromShortLink(link.getShortLink())).thenReturn(link.getLongLink());

        ResultActions resultActions = mockMvc.perform(get("/" + link.getShortLink())
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(link.getLongLink()));
    }
}
