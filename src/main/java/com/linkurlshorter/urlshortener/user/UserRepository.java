package com.linkurlshorter.urlshortener.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository interface for managing users in the database.
 *
 * <p>Extends {@link JpaRepository} to inherit common CRUD operations for user entities.
 *
 * <p>Provides custom query methods for retrieving user entities by email.
 *
 * <p><strong>Author:</strong> Artem Poliakov
 *
 * @see User
 */

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);
}
