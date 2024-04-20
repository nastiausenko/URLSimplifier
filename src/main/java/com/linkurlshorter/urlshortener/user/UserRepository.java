package com.linkurlshorter.urlshortener.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing users in the database.
 *
 * <p>Extends {@link JpaRepository} to inherit common CRUD operations for user entities.
 *
 * <p>Provides custom query methods for retrieving user entities by email.
 * Created on: 13.04.2024
 *
 * @author Artem Poliakov
 * @version 1.0
 * @see User
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    /**
     * Returns Optional of user by their email address.
     * Created on: 13.04.2024
     */
    Optional<User> findByEmail(String email);

    /**
     * Dynamically updates the corresponding row in the users table, modifying only those fields which are not null
     * in the param User entity. Null fields will be ignored and not included in the modifying request.
     * <strong>The row is searched by email param, so email param should be NOT NULL</strong>
     *
     * @param entity the User entity containing the fields to be updated. Only non-null fields will be included in the update.
     * @param email  the String email by which the User row can be found in database
     * @return the number of user records updated in the database.
     */
    @Transactional
    @Modifying
    @Query("UPDATE User u SET " +
            "u.email = CASE WHEN :#{#entity.email} IS NOT NULL THEN :#{#entity.email} ELSE u.email END, " +
            "u.password = CASE WHEN :#{#entity.password} IS NOT NULL THEN :#{#entity.password} ELSE u.password END, " +
            "u.role = CASE WHEN :#{#entity.role} IS NOT NULL THEN :#{#entity.role} ELSE u.role END " +
            "WHERE u.email = :email")
    int updateUserByEmailDynamically(@Param("entity") User entity, @Param("email") String email);
}
