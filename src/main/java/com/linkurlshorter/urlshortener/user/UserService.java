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
     * Updates a user's information dynamically based on the provided User object, specifically identified by their email address.
     *
     * <p>
     * This method checks if the provided User object contains a non-null email address. If it does,
     * the user's information is updated dynamically using the {@link UserRepository#updateUserByEmailDynamically(User, String)} method.
     * If the email address is null, a {@link NullEmailException} is thrown.
     * </p>
     *
     * @param user the User object containing the updated information.
     * @param email the String email by which the User row can be found in database
     * @return the number of user records updated in the database.
     * @throws NullEmailException if the email address in the provided User object is null.
     * @see UserRepository#updateUserByEmailDynamically(User, String)
     */
    public int updateByEmailDynamically(User user, String email){
        if(Objects.nonNull(email)){
            return userRepository.updateUserByEmailDynamically(user, email);
        } else{
            throw new NullEmailException();
        }
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