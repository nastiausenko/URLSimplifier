package com.linkurlshorter.urlshortener.link;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * Dynamically updates a link entity specified by its short link.
     * Dynamically updates the corresponding row in the links table, modifying only those fields which are not null
     * in the param Link entity. Null fields will be ignored and not included in the modifying request.
     * <strong>Does not update Id and UserId fields!!!</strong>
     * @param link       The link entity containing updated fields.
     * @param shortLink  The short link of the link entity to update.
     * @return The number of link entities updated.
     */
    @Transactional
    @Modifying
    @Query("UPDATE Link l SET " +
            "l.longLink = CASE WHEN :#{#link.longLink} IS NOT NULL THEN :#{#link.longLink} ELSE l.longLink END, " +
            "l.shortLink = CASE WHEN :#{#link.shortLink} IS NOT NULL THEN :#{#link.shortLink} ELSE l.shortLink END, " +
//            "l.createdTime = CASE WHEN :#{#link.createdTime} IS NOT NULL THEN :#{#link.createdTime} ELSE l.createdTime END, " +
//            "l.expirationTime = CASE WHEN :#{#link.expirationTime} IS NOT NULL THEN :#{#link.expirationTime} ELSE l.expirationTime END, " +
            "l.statistics = CASE WHEN :#{#link.statistics} IS NOT NULL THEN :#{#link.statistics} ELSE l.statistics END, " +
            "l.status = CASE WHEN :#{#link.status} IS NOT NULL THEN :#{#link.status} ELSE l.status END " +
            "WHERE l.shortLink = :shortLink")
    int updateLinkByShortLinkDynamically(@Param("link") Link link, @Param("shortLink") String shortLink);
}
