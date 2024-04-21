package com.linkurlshorter.urlshortener.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the {@link UserRepository} class.
 *
 * @author Ivan Shalaiev
 */
@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.0-alpine");
    @Autowired
    UserRepository userRepository;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(UUID.fromString("84991c79-f6a9-4b7b-b1b4-0d66c0b92c81"))
                .email("user1@example.com")
                .password("$2a$12$7Cp4On1DBNyCkz4TaZYc3O.A.CBKi4WXgXnlI4SD0yn7CgBX5Gd6O")
                .role(UserRole.USER)
                .build();
    }

    /**
     * Test to verify that the database container is created and running.
     */
    @Test
    void connectionEstablished() {
        assertThat(container.isCreated()).isTrue();
        assertThat(container.isRunning()).isTrue();
    }

    /**
     * Test to verify the {@link UserRepository#findByEmail(String)} method.
     * It ensures that the findByEmail method returns the correct user entity when provided with a valid email.
     */
    @Test
    void thatFindByEmailWorksCorrectly() {
        User userForFind = userRepository.findByEmail("user1@example.com").get();
        assertThat(userForFind)
                .isNotNull()
                .isEqualTo(user);
    }

    /**
     * Test to verify that the {@link UserRepository#updateUserByEmailDynamically(User, String)} method
     * works correctly when updating all user properties.
     * It ensures that all user properties (id, email, password, role) are updated successfully
     * and persist in the database.
     */
    @Test
    void thatUpdateUserByEmailDynamicallyWorksCorrectly() {
        User userForUpdate = User.builder()
                .id(UUID.fromString("84991c79-f6a9-4b7b-b1b4-0d66c0b92c81"))
                .email("newuser@example.com")
                .password("newpassword")
                .role(UserRole.ADMIN)
                .build();
        userRepository.updateUserByEmailDynamically(userForUpdate, user.getEmail());
        User userForFind = userRepository.findByEmail("newuser@example.com").get();
        assertThat(userForFind)
                .isNotNull()
                .isEqualTo(userForUpdate);
    }

    /**
     * Test to verify that the {@link UserRepository#updateUserByEmailDynamically(User, String)} method
     * works correctly when given only the email.
     * It ensures that the user's email is updated successfully and persists in the database.
     */
    @Test
    void thatUpdateUserByEmailDynamicallyWorksCorrectlyWhenGivenOnlyEmail() {
        String email = "newuser@example.com";
        User userForUpdate = User.builder()
                .email(email)
                .build();
        userRepository.updateUserByEmailDynamically(userForUpdate, user.getEmail());
        user.setEmail(email);
        User userForFind = userRepository.findByEmail(email).get();
        assertThat(userForFind)
                .isNotNull()
                .isEqualTo(user);
    }

    /**
     * Test to verify that the {@link UserRepository#updateUserByEmailDynamically(User, String)} method
     * works correctly when given only the password.
     * It ensures that the user's password is updated successfully and persists in the database.
     */
    @Test
    void thatUpdateUserByEmailDynamicallyWorksCorrectlyWhenGivenOnlyPassword() {
        String password = "newpassword";
        User userForUpdate = User.builder()
                .password(password)
                .build();
        userRepository.updateUserByEmailDynamically(userForUpdate, user.getEmail());
        user.setPassword(password);
        User userForFind = userRepository.findByEmail(user.getEmail()).get();
        assertThat(userForFind)
                .isNotNull()
                .isEqualTo(user);
    }

    /**
     * Test to verify that the {@link UserRepository#updateUserByEmailDynamically(User, String)} method
     * works correctly when given only the role.
     * It ensures that the user's role is updated successfully and persists in the database.
     */
    @Test
    void thatUpdateUserByEmailDynamicallyWorksCorrectlyWhenGivenOnlyRole() {
        User userForUpdate = User.builder()
                .role(UserRole.ADMIN)
                .build();
        userRepository.updateUserByEmailDynamically(userForUpdate, user.getEmail());
        user.setRole(UserRole.ADMIN);
        User userForFind = userRepository.findByEmail(user.getEmail()).get();
        assertThat(userForFind)
                .isNotNull()
                .isEqualTo(user);
    }

    /**
     * Test to verify that the {@link UserRepository#updateUserByEmailDynamically(User, String)} method
     * works correctly when given a null user object.
     * It ensures that no changes are made to the user entity in the database.
     */
    @Test
    void thatUpdateUserByEmailDynamicallyWorksCorrectlyWhenGivenNull() {
        User userForUpdate = User.builder().build();
        userRepository.updateUserByEmailDynamically(userForUpdate, user.getEmail());
        User userForFind = userRepository.findByEmail(user.getEmail()).get();
        assertThat(userForFind)
                .isNotNull()
                .isEqualTo(user);
    }
}