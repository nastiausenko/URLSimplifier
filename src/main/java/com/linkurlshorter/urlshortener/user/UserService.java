package com.linkurlshorter.urlshortener.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

/**
 * Service class providing operations for managing users.
 * <p>
 * This class allows for saving, updating, finding by ID or email, and deleting users.
 * </p>
 *
 * @author Artem Poliakov
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    /**
     * Saves a user entity.
     *
     * @param user the user entity to save
     * @return the saved user entity
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Updates a user entity.
     *
     * @param user the user entity to update
     * @return the updated user entity
     */
    public User update(User user) {
        return userRepository.save(user);
    }

    /**
     * Finds a user entity by its ID.
     *
     * @param id the UUID id of the user to find
     * @return the found user entity
     * @throws NoUserFoundByIdException if no user is found with the provided ID
     */
    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(NoUserFoundByIdException::new);
    }

    /**
     * Finds a user entity by its email.
     *
     * @param email the email of the user to find
     * @return the found user entity
     * @throws NoUserFoundByEmailException if no user is found with the provided email
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(NoUserFoundByEmailException::new);
    }

    /**
     * Deletes a user entity by its ID.
     *
     * @param id the UUID id of the user to delete
     * @return true if the user is successfully deleted, false otherwise
     */
    public boolean deleteById(UUID id) {
        userRepository.deleteById(id);
        return true;
    }
}