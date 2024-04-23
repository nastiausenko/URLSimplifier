package com.linkurlshorter.urlshortener.link;

import com.linkurlshorter.urlshortener.user.User;
import com.linkurlshorter.urlshortener.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
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

    @Mock
    private JedisPool jedisPool;

    @Mock
    private Jedis jedis;

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
        when(linkRepository.save(link)).thenReturn(link);

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
        assertThatThrownBy(() ->  linkService.deleteByShortLink("https://link/short"))
                .isInstanceOf(NoLinkFoundByShortLinkException.class);
    }

    /**
     * Test case for the {@link LinkService#findAllByUserId(UUID)} method.
     */
    @Test
    void findByAllByUserIdTest() {
        UUID userId = UUID.fromString("84991c79-f6a9-4b7b-b1b4-0d66c0b92c81");
        User user = User.builder().id(userId).build();
        List<Link> userLinks = Arrays.asList(
                Link.builder().id(UUID.randomUUID()).user(user).build(),
                Link.builder().id(UUID.randomUUID()).user(user).build(),
                Link.builder().id(UUID.randomUUID()).user(user).build()
        );

        when(linkRepository.findAllByUserId(userId)).thenReturn(userLinks);
        List<Link> foundLinks = linkService.findAllByUserId(userId);

        assertThat(foundLinks).isNotNull().isEqualTo(userLinks);
        verify(linkRepository, times(1)).findAllByUserId(userId);
    }

    /**
     * Test case for the {@link LinkService#findAllByUserId(UUID)} method when the provided user is null.
     */
    @Test
    void findAllByNullUserTest() {
        assertThatThrownBy(() -> linkService.findAllByUserId(null))
                .isInstanceOf(NullLinkPropertyException.class);
    }

    /**
     * Test case for the {@link LinkService#getLinkUsageStatsByUserId(UUID)} method.
     */
    @Test
    void getLinkUsageStatsByUserIdTest() {
        UUID userId = UUID.randomUUID();
        List<LinkStatisticsDto> expectedStats = Collections.singletonList(
                new LinkStatisticsDto(UUID.randomUUID(), "shortLink1", 10)
        );
        when(linkRepository.getLinkUsageStatsForUser(userId)).thenReturn(expectedStats);

        List<LinkStatisticsDto> actualStats = linkService.getLinkUsageStatsByUserId(userId);

        assertThat(actualStats).isNotNull().isEqualTo(expectedStats);
        verify(linkRepository, times(1)).getLinkUsageStatsForUser(userId);
    }

    /**
     * Test case for the {@link LinkService#getLinkUsageStatsByUserId(UUID)} method when the provided id is null.
     */
    @Test
    void getLinkUsageStatsByUserIdNullTest() {
        assertThatThrownBy(() -> linkService.getLinkUsageStatsByUserId(null))
                .isInstanceOf(NullLinkPropertyException.class);
    }

    /**
     * Test case for the {@link LinkService#deleteByShortLink(String)} method.
     */
    @Test
    void deleteByShortLinkTest() {
        when(jedisPool.getResource()).thenReturn(jedis);
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

    /**
     * Test case for the {@link LinkService#deleteById(UUID)} method.
     */
    @Test
    void deleteByIdTest() {
        assertAll(() -> linkService.deleteById(link.getId()));
        verify(linkRepository, times(1)).deleteById(link.getId());
    }

    /**
     * Test case for the {@link LinkService#deleteById(UUID)} method when the
     * provided id is null.
     */
    @Test
    void deleteByNullIdTest() {
        assertThatThrownBy(() -> linkService.deleteById(null))
                .isInstanceOf(NullLinkPropertyException.class);
    }


    /**
     * Test case for the {@link LinkService#doesLinkExist(String)} method.
     */
    @Test
    void findByExistUniqueLinkTest() {
        when(linkRepository.findByShortLink(link.getShortLink())).thenReturn(Optional.ofNullable(link));
        linkService.doesLinkExist(link.getShortLink());
        assertThat(LinkStatus.ACTIVE).isEqualTo(link.getStatus());
    }

    /**
     * Test case for the {@link LinkService#doesLinkExist(String)} method when there is no link
     * found by short link.
     */
    @Test
    void findByExistUniqueLinkNotFoundTest() {
        assertThatThrownBy(() -> linkService.doesLinkExist("short"))
                .isInstanceOf(NoLinkFoundByShortLinkException.class);
    } //TODO: it falls, method was completely changed

    /**
     * Test case for the {@link LinkService#doesLinkExist(String)} method when the provided link status
     * is not active.
     */
    @Test
    void findByExistUniqueLinkInactiveTest() {
        link.setStatus(LinkStatus.INACTIVE);
        when(linkRepository.findByShortLink(link.getShortLink())).thenReturn(Optional.ofNullable(link));

        assertThatThrownBy(() -> linkService.doesLinkExist(link.getShortLink()))
                .isInstanceOf(NullLinkPropertyException.class);
    }            //TODO: it falls, method was completely changed
}
