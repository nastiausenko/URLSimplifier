package com.linkurlshorter.urlshortener.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing users in the database.
 *
 * <p>Extends {@link JpaRepository} to inherit common CRUD operations for user entities.
 *
 * <p>Provides custom query methods for retrieving user entities by email.
 * Created on: 13.04.2024
 * @author Artem Poliakov
 * @version 1.0
 * @see User
 */

public interface UserRepository extends JpaRepository<User, UUID> {
    /**
     * Returns Optional of user by their email address.
     * Created on: 13.04.2024
     * */
    Optional<User> findByEmail(String email);
}
