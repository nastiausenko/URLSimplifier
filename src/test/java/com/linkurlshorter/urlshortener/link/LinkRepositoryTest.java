package com.linkurlshorter.urlshortener.link;

import com.linkurlshorter.urlshortener.user.User;
import com.linkurlshorter.urlshortener.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LinkRepositoryTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.0-alpine");
    @Autowired
    LinkRepository linkRepository;
    private Link link;
    @BeforeEach
    void setUp() {
        link = Link.builder()
                .id(UUID.fromString("3053e49b-6da3-4389-9d06-23b2d57b6f25"))
                .longLink("http://example.com/page1")
                .shortLink("http://linkshortener/shortlink1")
                .user(User.builder()
                        .id(UUID.fromString("84991c79-f6a9-4b7b-b1b4-0d66c0b92c81"))
                        .email("user1@example.com")
                        .password("password1")
                        .role(UserRole.USER)
                        .build())
                .createdTime(LocalDateTime.of(2024, 4, 13, 10, 0))
                .expirationTime(LocalDateTime.of(2024, 5, 16, 8, 0))
                .statistics(100)
                .status(LinkStatus.ACTIVE)
                .build();
    }

    @Test
    void connectionEstablished() {
        assertThat(container.isCreated()).isTrue();
        assertThat(container.isRunning()).isTrue();
    }
    @Test
    void thatDeleteAllWorksCorrectly() {
        linkRepository.deleteAll();
        assertThat(linkRepository.findAll()).isEmpty();
    }

    @Test
    void thatFindByIdWorksCorrectly() {
        Link found = linkRepository.findById(link.getId()).get();

        assertThat(found)
                .isNotNull()
                .isEqualTo(link);
    }
    @Test
    void thatFindByShortLinkWorksCorrectly() {
        Link found = linkRepository.findByShortLink(link.getShortLink()).get();
        assertThat(found)
                .isNotNull()
                .isEqualTo(link);
    }
    @Test
    void thatFindAllWorksCorrectly() {
        List<Link> links = linkRepository.findAll();
        assertThat(links).isNotNull()
                .hasSize(5);
    }
    @Test
    void thatDeleteWorksCorrectly() {

        linkRepository.delete(link);
        assertThat(linkRepository.findById(link.getId())).isEmpty();
    }
    @Test
    void thatSaveWorksCorrectly() {
        Link linkForSave = Link.builder()
                .longLink("http://example.com/page1")
                .shortLink("http://linkshortener/newshortlink1")
                .user(User.builder()
                        .id(UUID.fromString("84991c79-f6a9-4b7b-b1b4-0d66c0b92c81"))
                        .email("user1@example.com")
                        .password("password1")
                        .role(UserRole.USER)
                        .build())
                .createdTime(LocalDateTime.of(2024, 4, 13, 10, 0))
                .expirationTime(LocalDateTime.of(2024, 5, 16, 8, 0))
                .statistics(100)
                .status(LinkStatus.ACTIVE)
                .build();
        Link savedLink = linkRepository.save(linkForSave);
        assertThat(linkRepository.findById(savedLink.getId()))
                .isPresent()
                .isEqualTo(Optional.of(savedLink));
    }
    @Test
    void thatUpdateLinkByShortLinkDynamicallyWorksCorrectly() {
        Link linkForCompare = Link.builder()
                .id(UUID.fromString("3053e49b-6da3-4389-9d06-23b2d57b6f25"))
                .longLink("http://example.com/newPage")
                .shortLink("http://linkshortener/newshortlink")
                .user(User.builder()
                        .id(UUID.fromString("84991c79-f6a9-4b7b-b1b4-0d66c0b92c81"))
                        .email("user1@example.com")
                        .password("password1")
                        .role(UserRole.USER)
                        .build())
                .createdTime(LocalDateTime.of(2024, 4, 13, 10, 0))
                .expirationTime(LocalDateTime.of(2024, 5, 16, 8, 0))
                .statistics(101)
                .status(LinkStatus.ACTIVE)
                .build();

        Link linkForSave = Link.builder()
                .longLink("http://example.com/newPage")
                .shortLink("http://linkshortener/newshortlink")
                .expirationTime(LocalDateTime.of(2024, 5, 16, 8, 0))
                .statistics(101)
                .status(LinkStatus.ACTIVE)
                .build();

        linkRepository.updateLinkByShortLinkDynamically(linkForSave, link.getShortLink());
        assertThat(linkRepository.findByShortLink(linkForSave.getShortLink()))
                .isPresent()
                .isEqualTo(Optional.of(linkForCompare));
    }
}