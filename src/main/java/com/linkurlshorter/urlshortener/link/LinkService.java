package com.linkurlshorter.urlshortener.link;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.time.LocalDateTime;
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
    private void updateLinkStatsAndSave(Link link, Jedis jedis) {
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
     * Retrieves a link entity by its ID.
     *
     * @param id The ID of the link entity to retrieve.
     * @return The retrieved link entity.
     * @throws NullLinkPropertyException If the 'id' parameter is null.
     * @throws NoLinkFoundByIdException  If no link is found with the given ID.
     * @throws DeletedLinkException      If the retrieved link has been marked as deleted.
     */
    public Link findById(UUID id) { // **Unused method**
        if (Objects.isNull(id)) {
            throw new NullLinkPropertyException();
        }
        Link link = linkRepository.findById(id).orElseThrow(NoLinkFoundByIdException::new);
        if (link.getStatus() == LinkStatus.DELETED) {
            throw new DeletedLinkException();
        }
        return link;
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
        return link;
    }

    public List<Link> findAllByUserId(UUID userId) {
        if (Objects.isNull(userId)) {
            throw new NullLinkPropertyException();
        }
        return linkRepository.findAllByUserId(userId);
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

    public void deleteById(UUID id) { // **Unused method**
        if (Objects.isNull(id)) {
            throw new NullLinkPropertyException();
        }
        linkRepository.deleteById(id);
    }

    /**
     * Searches for a unique existing link by a short link.
     * If an active link is found for the specified short link, returns that link.
     *
     * @param shortLink A string representing the short link to be searched.
     * @return The active link found for the specified short link.
     * @throws NoLinkFoundByShortLinkException If no link was found by the short link.
     * @throws NullLinkPropertyException       If the found link does not have the ACTIVE status.
     */
    public Link findByExistUniqueLink(String shortLink) {
        Link existingLink = linkRepository.findByShortLink(shortLink).orElseThrow(NoLinkFoundByShortLinkException::new);
        if (existingLink.getStatus() == LinkStatus.ACTIVE) {
            return existingLink;
        } else {
            throw new NullLinkPropertyException();
        }
    }
}
