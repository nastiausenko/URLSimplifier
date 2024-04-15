package com.linkurlshorter.urlshortener.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

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
                .password("password1")
                .role(UserRole.USER)
                .build();
    }

    @Test
    void connectionEstablished() {
        assertThat(container.isCreated()).isTrue();
        assertThat(container.isRunning()).isTrue();
    }

    @Test
    void thatFindByEmailWorksCorrectly() {
        User userForFind = userRepository.findByEmail("user1@example.com").get();
        assertThat(userForFind)
                .isNotNull()
                .isEqualTo(user);
    }

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
    @Test
    void thatUpdateUserByEmailDynamicallyWorksCorrectlyWhenGivenOnlyEmail(){
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
    @Test
    void thatUpdateUserByEmailDynamicallyWorksCorrectlyWhenGivenOnlyPassword(){
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
    @Test
    void thatUpdateUserByEmailDynamicallyWorksCorrectlyWhenGivenOnlyRole(){
        UserRole role = UserRole.ADMIN;
        User userForUpdate = User.builder()
                .role(role)
                .build();
        userRepository.updateUserByEmailDynamically(userForUpdate, user.getEmail());
        user.setRole(role);
        User userForFind = userRepository.findByEmail(user.getEmail()).get();
        assertThat(userForFind)
                .isNotNull()
                .isEqualTo(user);
    }
}