package com.linkurlshorter.urlshortener.link;

import com.linkurlshorter.urlshortener.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing links in the database.
 *
 * <p>Extends {@link JpaRepository} to inherit common CRUD operations for link entities. Created on: 13.04.2024
 *
 * @author Artem Poliakov
 * @version 1.0
 * @see Link
 */
@Repository
public interface LinkRepository extends JpaRepository<Link, UUID> {

    /**
     * Retrieves a link entity by its short link.
     *
     * @param shortLink The short link of the link entity to retrieve.
     * @return An {@link java.util.Optional} containing the retrieved link entity, or empty if no link is found with the specified short link.
     */
    Optional<Link> findByShortLink(String shortLink);

    @Query("SELECT l from Link l WHERE l.user.id = :userId AND l.status <> 'DELETED'")
    List<Link> findAllByUserId(@Param(value = "userId") UUID userId);

    List<Link> findAllByUser(User user);

    @Query("SELECT new com.linkurlshorter.urlshortener.link.LinkStatisticsDto(l.id, l.shortLink, l.statistics)" +
            " FROM Link l WHERE l.user.id = :userId AND l.status <> 'DELETED'")
    List<LinkStatisticsDto> getLinkUsageStatsForUser(@Param(value = "userId") UUID userId);
    /**
     * Deletes a link entity by its ID.
     *
     * @param id The ID of the link entity to delete.
     */
    void deleteById(UUID id);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM links WHERE short_link = :shortLink")
    int countLinksByShortLink(@Param(value = "shortLink") String shortLink);
}
