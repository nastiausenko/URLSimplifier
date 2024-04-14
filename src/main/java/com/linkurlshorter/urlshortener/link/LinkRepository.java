package com.linkurlshorter.urlshortener.link;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
     * Finds a link by its short link.
     *
     * @param shortLink the short link of the link to find
     * @return the link entity with the given short link
     */
    Optional<Link> findByShortLink(String shortLink);
}
