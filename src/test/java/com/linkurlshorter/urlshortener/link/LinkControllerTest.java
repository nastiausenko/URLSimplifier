package com.linkurlshorter.urlshortener.link;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkurlshorter.urlshortener.TestConfig;
import com.linkurlshorter.urlshortener.security.SecurityConfig;
import com.linkurlshorter.urlshortener.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for {@link LinkController} class.
 *
 * @author Anastasiia Usenko
 */
@WebMvcTest(controllers = LinkController.class)
@Import({SecurityConfig.class, TestConfig.class})
class LinkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LinkService linkService;

    @MockBean
    private UserService userService;


    private User user;
    private Link link;

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

        link = Link.builder()
                .id(UUID.fromString("3053e49b-6da3-4389-9d06-23b2d57b6f25"))
                .longLink("https://www.youtube.com")
                .shortLink("short-link-1")
                .user(user)
                .createdTime(LocalDateTime.of(2024, 4, 13, 10, 0))
                .expirationTime(LocalDateTime.of(2024, 5, 16, 8, 0))
                .statistics(100)
                .status(LinkStatus.ACTIVE)
                .build();
    }

    /**
     * Test case for the {@link LinkController#createLink(CreateLinkRequest)} method.
     */
    @Test
    @WithMockUser
    void createLinkTest() throws Exception {
        when(userService.findByEmail(any())).thenReturn(user);
        when(linkService.save(any())).thenReturn(link);

        CreateLinkRequest request = new CreateLinkRequest("https://www.example.com");

        ResultActions resultActions = mockMvc.perform(post("/api/V1/link/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("ok"))
                .andExpect(jsonPath("$.shortLink").exists());
    }

    /**
     * Test case for the {@link LinkController#createLink(CreateLinkRequest)} method when
     * an error occurs during the link creation process.
     */
    @Test
    @WithMockUser
    void createLinkFailedTest() throws Exception {
        CreateLinkRequest request = new CreateLinkRequest("https://www.example.com");

        when(userService.findByEmail(any())).thenReturn(user);
        when(linkService.save(any())).thenThrow(new RuntimeException("Short link already exists"));

        ResultActions resultActions = mockMvc.perform(post("/api/V1/link/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        resultActions.andExpect(status().isInternalServerError());
    }

    /**
     * Test case for the {@link LinkController#deleteLink(UUID)} method.
     */
    @Test
    @WithMockUser
    void deleteLinkTest() throws Exception {
        when(userService.findByEmail(any())).thenReturn(user);
        when(linkService.findById(link.getId())).thenReturn(link);
        doNothing().when(linkService).deleteById(link.getId());

        ResultActions resultActions = mockMvc.perform(post("/api/V1/link/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", String.valueOf(link.getId())));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("ok"));
    }

    /**
     * Test case for the {@link LinkController#deleteLink(UUID)} method when
     * the authenticated user does not have rights.
     */
    @Test
    @WithMockUser
    void deleteLinkForbiddenTest() throws Exception {
        User newUser = User.builder()
                .id(UUID.fromString("84991c79-f6a9-4b7b-b1b4-0d66c0b92c83"))
                .email("test3@gmail.com")
                .password("Password3")
                .role(UserRole.USER)
                .build();
        when(userService.findByEmail(any())).thenReturn(newUser);
        when(linkService.findById(link.getId())).thenReturn(link);
        doNothing().when(linkService).deleteById(link.getId());

        ResultActions resultActions = mockMvc.perform(post("/api/V1/link/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", String.valueOf(link.getId())));

        resultActions.andExpect(status().isForbidden());
    }

    /**
     * Test case for the {@link LinkController#editLinkContent(EditLinkContentRequest)} method.
     */
    @Test
    @WithMockUser
    void editLinkContentTest() throws Exception {
        when(userService.findByEmail(any())).thenReturn(user);
        when(linkService.findById(link.getId())).thenReturn(link);

        EditLinkContentRequest request = new EditLinkContentRequest(link.getId(), "short-link-2");
        when(linkService.update(link)).thenReturn(link);

        ResultActions resultActions = mockMvc.perform(post("/api/V1/link/edit/content")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", String.valueOf(link.getId()))
                .content(objectMapper.writeValueAsString(request)));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("ok"));
    }

    /**
     * Test case for the {@link LinkController#editLinkContent(EditLinkContentRequest)} method when
     * the authenticated user does not have rights.
     */
    @Test
    @WithMockUser
    void editLinkContentForbiddenTest() throws Exception {
        User newUser = User.builder()
                .id(UUID.fromString("84991c79-f6a9-4b7b-b1b4-0d66c0b92c83"))
                .email("test3@gmail.com")
                .password("Password3")
                .role(UserRole.USER)
                .build();
        when(userService.findByEmail(any())).thenReturn(newUser);
        when(linkService.findById(link.getId())).thenReturn(link);

        EditLinkContentRequest request = new EditLinkContentRequest(link.getId(), "short-link-2");
        when(linkService.update(link)).thenReturn(link);

        ResultActions resultActions = mockMvc.perform(post("/api/V1/link/edit/content")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", String.valueOf(link.getId()))
                .content(objectMapper.writeValueAsString(request)));

        resultActions.andExpect(status().isForbidden());
    }

    /**
     * Test case for the {@link LinkController#editLinkContent(EditLinkContentRequest)} method when
     * the status of the link is not ACTIVE.
     */
    @Test
    @WithMockUser
    void editDeletedLinkContentTest() throws Exception {
        link.setStatus(LinkStatus.DELETED);
        when(userService.findByEmail(any())).thenReturn(user);
        when(linkService.findById(link.getId())).thenReturn(link);

        EditLinkContentRequest request = new EditLinkContentRequest(link.getId(), "short-link-2");
        when(linkService.update(link)).thenReturn(link);

        ResultActions resultActions = mockMvc.perform(post("/api/V1/link/edit/content")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", String.valueOf(link.getId()))
                .content(objectMapper.writeValueAsString(request)));

        resultActions.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.exceptionMessage").value("Link status is invalid for the operation"));
    }

    /**
     * Test case for the {@link LinkController#refreshLink(UUID)} method.
     */
    @Test
    @WithMockUser
    void refreshLinkTest() throws Exception {
        when(userService.findByEmail(any())).thenReturn(user);
        when(linkService.findById(link.getId())).thenReturn(link);
        when(linkService.update(link)).thenReturn(link);

        ResultActions resultActions = mockMvc.perform(post("/api/V1/link/edit/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", String.valueOf(link.getId())));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("ok"));
    }

    /**
     * Test case for the {@link LinkController#refreshLink(UUID)} method when
     * the authenticated user does not have rights.
     */
    @Test
    @WithMockUser
    void refreshLinkForbiddenTest() throws Exception {
        User newUser = User.builder()
                .id(UUID.fromString("84991c79-f6a9-4b7b-b1b4-0d66c0b92c83"))
                .email("test3@gmail.com")
                .password("Password3")
                .role(UserRole.USER)
                .build();
        when(userService.findByEmail(any())).thenReturn(newUser);
        when(linkService.findById(link.getId())).thenReturn(link);
        when(linkService.update(link)).thenReturn(link);

        ResultActions resultActions = mockMvc.perform(post("/api/V1/link/edit/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", String.valueOf(link.getId())));

        resultActions.andExpect(status().isForbidden());
    }

    /**
     * Test case for the {@link LinkController#refreshLink(UUID)} method when
     * the status of the link is not ACTIVE.
     */
    @Test
    @WithMockUser
    void refreshDeletedLinkTest() throws Exception {
        link.setStatus(LinkStatus.DELETED);
        when(userService.findByEmail(any())).thenReturn(user);
        when(linkService.findById(link.getId())).thenReturn(link);
        when(linkService.update(link)).thenReturn(link);

        ResultActions resultActions = mockMvc.perform(post("/api/V1/link/edit/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", String.valueOf(link.getId())));

        resultActions.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.exceptionMessage").value("The link has already been deleted, " +
                                                       "no operations are allowed"));
    }

    /**
     * Test case for the {@link LinkController#getInfoByShortLink(String)} method.
     */
    @Test
    @WithMockUser
    void getInfoByShortLinkTest() throws Exception {
        when(userService.findByEmail(any())).thenReturn(user);
        when(linkService.findById(any())).thenReturn(link);
        when(linkService.findByShortLink(link.getShortLink())).thenReturn(link);

        ResultActions resultActions = mockMvc.perform(get("/api/V1/link/info")
                .contentType(MediaType.APPLICATION_JSON)
                .param("shortLink", link.getShortLink()));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("ok"))
                .andExpect(jsonPath("$.linkDtoList").isArray());
    }

    /**
     * Test case for the {@link LinkController#getInfoByShortLink(String)} method when
     * the authenticated user does not have rights.
     */
    @Test
    @WithMockUser
    void getInfoByShortLinkForbiddenTest() throws Exception {
        User newUser = User.builder()
                .id(UUID.fromString("84991c79-f6a9-4b7b-b1b4-0d66c0b92c83"))
                .email("test3@gmail.com")
                .password("Password3")
                .role(UserRole.USER)
                .build();
        when(userService.findByEmail(any())).thenReturn(newUser);
        when(linkService.findById(any())).thenReturn(link);
        when(linkService.findByShortLink(link.getShortLink())).thenReturn(link);

        ResultActions resultActions = mockMvc.perform(get("/api/V1/link/info")
                .contentType(MediaType.APPLICATION_JSON)
                .param("shortLink", link.getShortLink()));

        resultActions.andExpect(status().isForbidden());
    }

    /**
     * Test case for the {@link LinkController#getAllLinksForUser()} method.
     */
    @Test
    @WithMockUser
    void getAllLinksForUserTest() throws Exception {
        List<Link> userLinks = Arrays.asList(
                Link.builder().id(UUID.randomUUID()).user(user).build(),
                Link.builder().id(UUID.randomUUID()).user(user).build(),
                Link.builder().id(UUID.randomUUID()).user(user).build());

        when(userService.findByEmail(any())).thenReturn(user);
        when(linkService.findAllByUserId(user.getId())).thenReturn(userLinks);

        ResultActions resultActions = mockMvc.perform(get("/api/V1/link/all-links-info")
                .contentType(MediaType.APPLICATION_JSON)
                .param("userId", String.valueOf(user.getId())));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("ok"))
                .andExpect(jsonPath("$.linkDtoList").isArray())
                .andExpect(jsonPath("$.linkDtoList.length()").value(userLinks.size()));
    }

    /**
     * Test case for the {@link LinkController#getLinksStatsForUser()} method.
     */
    @Test
    @WithMockUser
    void getLinksStatsForUserTest() throws Exception {
        List<LinkStatisticsDto> stats = Arrays.asList(
                new LinkStatisticsDto(UUID.randomUUID(),"link1", 10),
                new LinkStatisticsDto(UUID.randomUUID(),"link2", 20)
        );

        when(userService.findByEmail(any())).thenReturn(user);
        when(linkService.getLinkUsageStatsByUserId(user.getId())).thenReturn(stats);
        ResultActions resultActions = mockMvc.perform(get("/api/V1/link/url-usage-top-for-user")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.linksStatsList").isArray())
                .andExpect(jsonPath("$.error").value("ok"));
    }
}