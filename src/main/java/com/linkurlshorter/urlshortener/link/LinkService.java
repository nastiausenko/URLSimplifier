package com.linkurlshorter.urlshortener.link;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkurlshorter.urlshortener.link.dto.LinkStatisticsDto;
import com.linkurlshorter.urlshortener.link.exception.DeletedLinkException;
import com.linkurlshorter.urlshortener.link.exception.InactiveLinkException;
import com.linkurlshorter.urlshortener.link.exception.NoLinkFoundByShortLinkException;
import com.linkurlshorter.urlshortener.link.exception.NullLinkPropertyException;
import com.linkurlshorter.urlshortener.link.validation.EndTimeLinkValidator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Service class for managing link entities.
 */
@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;
    private final JedisPool jedisPool;
    private final ObjectMapper mapper;

    /**
     * Retrieves the long link associated with the provided short link.
     *
     * <p>This method first attempts to fetch the long link from the Redis cache using the provided short link.
     * If the short link exists in the cache, it deserializes the stored link object using the ObjectMapper
     * and returns the long link. If the short link is not found in the cache, it queries the LinkRepository
     * to fetch the link from the database. After retrieving the link, it checks if the link status is active,
     * updates link statistics, and saves the link to the Redis cache. Finally, it returns the long link.
     *
     * <p>The method is annotated with {@link SneakyThrows} to suppress checked exceptions from the ObjectMapper.
     *
     * @param shortLink the short link for which to retrieve the long link
     * @return the long link associated with the short link
     * @throws InactiveLinkException if the retrieved link is inactive
     */
    @SneakyThrows
    public String getLongLinkFromShortLink(String shortLink) {
        try (Jedis jedis = jedisPool.getResource()) {
            Link link = jedis.exists(shortLink)
                    ? mapper.readValue(jedis.get(shortLink), Link.class)
                    : findByShortLink(shortLink);
            if (link.getStatus() == LinkStatus.INACTIVE) {
                throw new InactiveLinkException(shortLink);
            }
            if (link.getExpirationTime().isBefore(LocalDateTime.now())) {
                link.setStatus(LinkStatus.INACTIVE);
                jedis.set(link.getShortLink(), mapper.writeValueAsString(link));
                save(link);
                throw new InactiveLinkException(shortLink);
            }
            updateLinkStatsAndSave(link, jedis);
            return link.getLongLink();
        }
    }

    /**
     * Updates the link statistics, expiration time, and caches the link.
     *
     * <p>The method is annotated with {@link SneakyThrows} to suppress checked exceptions from the ObjectMapper.
     *
     * @param link the link to be updated
     */
    @SneakyThrows
    private void updateLinkStatsAndSave(@EndTimeLinkValidator Link link, Jedis jedis) {
        link.setStatistics(link.getStatistics() + 1);
        link.setExpirationTime(LocalDateTime.now().plusMonths(1));

        jedis.set(link.getShortLink(), mapper.writeValueAsString(link));
        save(link);
    }

    /**
     * Saves a link entity.
     *
     * @param link The link entity to save.
     * @return The saved link entity.
     * @throws NullLinkPropertyException If the 'link' parameter is null.
     */
    public Link save(Link link) {
        if (Objects.isNull(link)) {
            throw new NullLinkPropertyException();
        }
        return linkRepository.save(link);
    }

    /**
     * Updates a link entity.
     *
     * @param link The link entity to update.
     * @return The updated link entity.
     * @throws NullLinkPropertyException If the 'link' parameter is null.
     * @throws DeletedLinkException      If the link has been marked as deleted.
     */
    public Link update(Link link) {
        if (Objects.isNull(link)) {
            throw new NullLinkPropertyException();
        }

        if (link.getStatus() == LinkStatus.DELETED) {
            throw new DeletedLinkException();
        }
        return linkRepository.save(link);
    }

    public void updateRedisShortLink(String shortLink, String newShortLink) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (jedis.exists(shortLink)) {
                jedis.rename(shortLink, newShortLink);
            }
        }
    }

    @SneakyThrows
    public void updateRedisLink(String shortLink, Link link) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (jedis.exists(shortLink)) {
                jedis.set(shortLink, mapper.writeValueAsString(link));
            }
        }
    }

    /**
     * Retrieves a link entity by its short link.
     *
     * @param shortLink The short link of the link entity to retrieve.
     * @return The retrieved link entity.
     * @throws NullLinkPropertyException       If the 'shortLink' parameter is null.
     * @throws NoLinkFoundByShortLinkException If no link is found with the given short link.
     * @throws DeletedLinkException            If the retrieved link has been marked as deleted.
     */
    public Link findByShortLink(String shortLink) {
        if (Objects.isNull(shortLink)) {
            throw new NullLinkPropertyException();
        }
        Link link = linkRepository.findByShortLink(shortLink).orElseThrow(NoLinkFoundByShortLinkException::new);
        if (link.getStatus() == LinkStatus.DELETED) {
            throw new DeletedLinkException();
        }
        fixLinkStatusesAndReturnFixed(List.of(link));
        return link;
    }

    public List<Link> findAllByUserId(UUID userId) {
        if (Objects.isNull(userId)) {
            throw new NullLinkPropertyException();
        }
        List<Link> allByUserId = linkRepository.findAllByUserId(userId);
        fixLinkStatusesAndReturnFixed(allByUserId);
        return allByUserId;
    }

    /**
     * Retrieves a list of active links associated with the specified user ID and updates the status of expired links.
     * This method first checks if the provided user ID is null. If so, it throws a NullLinkPropertyException.
     * It then retrieves all active links associated with the given user ID from the LinkRepository.
     * Any links with expiration times before the current time are marked as inactive. All the rest truly active links are then returned.
     *
     * @param userId The ID of the user whose active links are to be retrieved.
     * @return A list of active Link objects associated with the specified user ID.
     * @throws NullLinkPropertyException if the provided user ID is null.
     */
    public List<Link> findAllActiveByUserId(UUID userId) {
        if (Objects.isNull(userId)) {
            throw new NullLinkPropertyException();
        }
        List<Link> allActiveByUserId = linkRepository.findAllActiveByUserId(userId);
        List<Link> fixedToInactive = fixLinkStatusesAndReturnFixed(allActiveByUserId);
        allActiveByUserId.removeAll(fixedToInactive);
        return allActiveByUserId;
    }

    public List<LinkStatisticsDto> getLinkUsageStatsByUserId(UUID userId) {
        if (Objects.isNull(userId)) {
            throw new NullLinkPropertyException();
        }
        return linkRepository.getLinkUsageStatsForUser(userId);
    }

    /**
     * Marks a link entity as deleted by its short link.
     *
     * @param shortLink The short link of the link entity to mark as deleted.
     * @throws NullLinkPropertyException       If the 'shortLink' parameter is null.
     * @throws NoLinkFoundByShortLinkException If no link is found with the given short link.
     * @throws DeletedLinkException            If the link has already been marked as deleted.
     */
    public void deleteByShortLink(String shortLink) {
        if (Objects.isNull(shortLink)) {
            throw new NullLinkPropertyException();
        }
        Link link = findByShortLink(shortLink);
        link.setStatus(LinkStatus.DELETED);

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.unlink(shortLink);
        }
        linkRepository.save(link);
    }

    /**
     * Searches for a unique existing link by a short link and returns true if such link exists, false otherwise
     *
     * @param shortLink A string representing the short link to be searched.
     * @return boolean true if link exists in database
     */
    public boolean doesLinkExist(String shortLink) {
        return linkRepository.findByShortLink(shortLink).isPresent();
    }

    private List<Link> fixLinkStatusesAndReturnFixed(List<Link> allActiveByUserId) {
        List<Link> toBeStatusFixed = new ArrayList<>();
        for (Link link : allActiveByUserId) {
            if (link.getExpirationTime().isBefore(LocalDateTime.now())) {
                link.setStatus(LinkStatus.INACTIVE);
                toBeStatusFixed.add(link);
            }
        }
        return toBeStatusFixed;
    }
}
