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

/**
 * Unit tests for {@link LinkService} class.
 *
 * @author Anastasiia Usenko
 */
@ExtendWith(MockitoExtension.class)
class LinkServiceTest {
    @Mock
    private LinkRepository linkRepository;

    @InjectMocks
    private LinkService linkService;

    private Link link;

    /**
     * Set up method to initialize test data before each test method.
     */
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

    /**
     * Test case for the {@link LinkService#save(Link)} method.
     */
    @Test
    void saveSuccessfulTest() {
        when(linkRepository.save(any(Link.class))).thenReturn(link);
        Link savedLink = linkService.save(link);

        assertThat(savedLink).isNotNull().isEqualTo(link);
        verify(linkRepository, times(1)).save(link);
    }

    /**
     * Test case for the {@link LinkService#save(Link)} method when the provided link is null.
     */
    @Test
    void saveFailedTest() {
        assertThatThrownBy(() -> linkService.save(null))
                .isInstanceOf(NullLinkPropertyException.class);
    }

    /**
     * Test case for the {@link LinkService#update(Link)} method.
     */
    @Test
    void updateSuccessfulTest() {
        when(linkRepository.findByShortLink(link.getShortLink())).thenReturn(Optional.of(link));
        when(linkRepository.save(any(Link.class))).thenReturn(link);

        Link savedLink = linkService.update(link);
        assertThat(savedLink).isNotNull().isEqualTo(link);
    }

    /**
     * Test case for the {@link LinkService#update(Link)} method when the provided link is null.
     */
    @Test
    void updateNullLinkTest() {
        assertThatThrownBy(() -> linkService.update(null))
                .isInstanceOf(NullLinkPropertyException.class);
    }

    /**
     * Test case for the {@link LinkService#update(Link)} method when the provided link is deleted.
     */
    @Test
    void updateDeletedLinkTest() {
        link.setStatus(LinkStatus.DELETED);
        when(linkRepository.findByShortLink(link.getShortLink())).thenReturn(Optional.of(link));

        assertThatThrownBy(() -> linkService.update(link))
                .isInstanceOf(DeletedLinkException.class);
    }

    /**
     * Test case for the {@link LinkService#findById(UUID)} method.
     */
    @Test
    void findByIdTest() {
        when(linkRepository.findById(link.getId())).thenReturn(Optional.ofNullable(link));
        Link foundLink = linkService.findById(link.getId());

        assertThat(foundLink).isNotNull().isEqualTo(link);
        verify(linkRepository, times(1)).findById(link.getId());
    }

    /**
     * Test case for the {@link LinkService#findById(UUID)} method when the link with provided id
     * does not exist.
     */
    @Test
    void findByIdNotFoundTest() {
        UUID nonExistentUserId = UUID.randomUUID();
        when(linkRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> linkService.findById(nonExistentUserId))
                .isInstanceOf(NoLinkFoundByIdException.class);
    }

    /**
     * Test case for the {@link LinkService#findById(UUID)} method when the link with provided id
     * is deleted.
     */
    @Test
    void findByIdDeletedTest() {
        link.setStatus(LinkStatus.DELETED);
        when(linkRepository.findById(link.getId())).thenReturn(Optional.of(link));

        assertThatThrownBy(() -> linkService.findById(link.getId()))
                .isInstanceOf(DeletedLinkException.class);
    }

    /**
     * Test case for the {@link LinkService#findById(UUID)} method when the provided id is null.
     */
    @Test
    void findByNullIdTest() {
        assertThatThrownBy(() -> linkService.findById(null))
                .isInstanceOf(NullLinkPropertyException.class);
    }

    /**
     * Test case for the {@link LinkService#findByShortLink(String)} method.
     */
    @Test
    void findByShortLinkTest() {
        when(linkRepository.findByShortLink(link.getShortLink())).thenReturn(Optional.of(link));
        Link foundLink = linkService.findByShortLink(link.getShortLink());

        assertThat(foundLink).isNotNull().isEqualTo(link);
        verify(linkRepository, times(1)).findByShortLink(link.getShortLink());
    }

    /**
     * Test case for the {@link LinkService#findByShortLink(String)} method when the
     * provided short link is null.
     */
    @Test
    void findByNullShortLinkTest() {
        assertThatThrownBy(() -> linkService.findByShortLink(null))
                .isInstanceOf(NullLinkPropertyException.class);
    }

    /**
     * Test case for the {@link LinkService#findByShortLink(String)} method when the
     * provided short link does not exist.
     */
    @Test
    void findByShortLinkNotFoundTest() {
        assertThatThrownBy(() ->  linkService.deleteByShortLink("http://link/short"))
                .isInstanceOf(NoLinkFoundByShortLinkException.class);
    }

    /**
     * Test case for the {@link LinkService#deleteByShortLink(String)} method.
     */
    @Test
    void deleteByShortLinkTest() {
        when(linkRepository.findByShortLink(link.getShortLink())).thenReturn(Optional.of(link));
        linkService.deleteByShortLink(link.getShortLink());
        assertThat(LinkStatus.DELETED).isEqualTo(link.getStatus());
    }

    /**
     * Test case for the {@link LinkService#deleteByShortLink(String)} method when the
     * provided short link is null.
     */
    @Test
    void deleteByNullShortLinkTest() {
        assertThatThrownBy(() -> linkService.deleteByShortLink(null))
                .isInstanceOf(NullLinkPropertyException.class);
    }
}
