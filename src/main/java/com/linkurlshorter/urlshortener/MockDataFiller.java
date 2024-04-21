package com.linkurlshorter.urlshortener;

import com.linkurlshorter.urlshortener.link.Link;
import com.linkurlshorter.urlshortener.link.LinkService;
import com.linkurlshorter.urlshortener.link.LinkStatus;
import com.linkurlshorter.urlshortener.user.User;
import com.linkurlshorter.urlshortener.user.UserRole;
import com.linkurlshorter.urlshortener.user.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class MockDataFiller {
    private final UserService userService;
    private final LinkService linkService;

    @PostConstruct
    void setMockData() {
        // Insert users
//        User user1 = User.builder()
//                .id(UUID.fromString("84991c79-f6a9-4b7b-b1b4-0d66c0b92c81"))
//                .email("user1@example.com")
//                .password("$2a$12$7Cp4On1DBNyCkz4TaZYc3O.A.CBKi4WXgXnlI4SD0yn7CgBX5Gd6O")
//                .role(UserRole.USER)
//                .build();
//
//        User user2 = User.builder()
//                .id(UUID.fromString("f6ff4ee4-92c4-49f3-97eb-d6c69a715325"))
//                .email("user2@example.com")
//                .password("$2a$12$7Cp4On1DBNyCkz4TaZYc3O.A.CBKi4WXgXnlI4SD0yn7CgBX5Gd6O")
//                .role(UserRole.USER)
//                .build();
//
//        User user3 = User.builder()
//                .id(UUID.fromString("b95cb1b3-1a7e-4e8b-a7ef-f3e20aef5f0a"))
//                .email("user3@example.com")
//                .password("$2a$12$7Cp4On1DBNyCkz4TaZYc3O.A.CBKi4WXgXnlI4SD0yn7CgBX5Gd6O")
//                .role(UserRole.ADMIN)
//                .build();
//
//        userService.save(user1);
//        userService.save(user2);
//        userService.save(user3);
//
//        // Insert links
//        Link link1 = new Link(UUID.fromString("3053e49b-6da3-4389-9d06-23b2d57b6f25"), "https://www.youtube.com", "short-link-1", user1, LocalDateTime.parse("2024-04-13T10:00:00"), LocalDateTime.parse("2024-05-16T08:00:00"), 100, LinkStatus.ACTIVE);
//        Link link2 = new Link(UUID.fromString("5c8d1659-2a63-4b5e-8a0f-af6aefbf0baf"), "https://chat.openai.com", "short-link-2", user2, LocalDateTime.parse("2024-04-14T10:00:00"), LocalDateTime.parse("2024-05-17T10:00:00"), 150, LinkStatus.ACTIVE);
//        Link link3 = new Link(UUID.fromString("3e486107-cbd3-45c0-8142-2e42342a1694"), "https://github.com", "short-link-3", user3, LocalDateTime.parse("2024-04-15T10:00:00"), LocalDateTime.parse("2024-05-19T10:00:00"), 200, LinkStatus.ACTIVE);
//        Link link4 = new Link(UUID.fromString("d2a8b2fc-0d25-4b13-9b4c-8b9ad443a5e1"), "https://www.codewars.com", "short-link-4", user1, LocalDateTime.parse("2024-04-13T10:00:00"), LocalDateTime.parse("2024-05-13T09:00:00"), 600, LinkStatus.ACTIVE);
//        Link link5 = new Link(UUID.fromString("9ef99e19-3986-4cf4-aa57-d6af46b72d7f"), "https://leetcode.com", "short-link-5", user3, LocalDateTime.parse("2024-04-16T10:00:00"), LocalDateTime.parse("2024-05-20T07:00:00"), 199, LinkStatus.INACTIVE);
//
//        linkService.save(link1);
//        linkService.save(link2);
//        linkService.save(link3);
//        linkService.save(link4);
//        linkService.save(link5);
    }
}
