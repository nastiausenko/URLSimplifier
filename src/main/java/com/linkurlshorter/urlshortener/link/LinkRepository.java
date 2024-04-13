package com.linkurlshorter.urlshortener.link;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository interface for managing links in the database.
 *
 * <p>Extends {@link JpaRepository} to inherit common CRUD operations for link entities.
 *
 * <p><strong>Author:</strong> Artem Poliakov
 *
 * @see Link
 */
public interface LinkRepository extends JpaRepository<Link, UUID> {
}
