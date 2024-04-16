package com.linkurlshorter.urlshortener.link;

import com.linkurlshorter.urlshortener.user.User;
import com.linkurlshorter.urlshortener.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LinkServiceTest {
    @Mock
    private LinkRepository linkRepository;

    @InjectMocks
    private LinkService linkService;

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
    void saveSuccessfulTest() {
        when(linkRepository.save(any(Link.class))).thenReturn(link);
        Link savedLink = linkService.save(link);

        assertThat(savedLink).isNotNull().isEqualTo(link);
        verify(linkRepository, times(1)).save(link);
    }

    @Test
    void saveFailedTest() {
        assertThatThrownBy(() -> linkService.save(null))
                .isInstanceOf(NullLinkPropertyException.class);
    }

    @Test
    void updateSuccessfulTest() {
        when(linkRepository.findByShortLink(link.getShortLink())).thenReturn(Optional.of(link));
        when(linkRepository.save(any(Link.class))).thenReturn(link);

        Link savedLink = linkService.update(link);
        assertThat(savedLink).isNotNull().isEqualTo(link);
    }

    @Test
    void updateNullLinkTest() {
        assertThatThrownBy(() -> linkService.update(null))
                .isInstanceOf(NullLinkPropertyException.class);
    }

    @Test
    void updateDeletedLinkTest() {
        link.setStatus(LinkStatus.DELETED);
        when(linkRepository.findByShortLink(link.getShortLink())).thenReturn(Optional.of(link));

        assertThatThrownBy(() -> linkService.update(link))
                .isInstanceOf(DeletedLinkException.class);
    }

    @Test
    void findByIdTest() {
        when(linkRepository.findById(link.getId())).thenReturn(Optional.ofNullable(link));
        Link foundLink = linkService.findById(link.getId());

        assertThat(foundLink).isNotNull().isEqualTo(link);
        verify(linkRepository, times(1)).findById(link.getId());
    }

    @Test
    void findByIdNotFoundTest() {
        UUID nonExistentUserId = UUID.randomUUID();
        when(linkRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> linkService.findById(nonExistentUserId))
                .isInstanceOf(NoLinkFoundByIdException.class);
    }

    @Test
    void findByIdDeletedTest() {
        link.setStatus(LinkStatus.DELETED);
        when(linkRepository.findById(link.getId())).thenReturn(Optional.of(link));

        assertThatThrownBy(() -> linkService.findById(link.getId()))
                .isInstanceOf(DeletedLinkException.class);
    }

    @Test
    void findByNullIdTest() {
        assertThatThrownBy(() -> linkService.findById(null))
                .isInstanceOf(NullLinkPropertyException.class);
    }
}
