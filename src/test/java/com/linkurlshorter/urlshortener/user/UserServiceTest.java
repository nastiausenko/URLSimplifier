package com.linkurlshorter.urlshortener.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserService} class.
 *
 * @author Anastasiia Usenko
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    private User user;

    /**
     * Set up method to initialize test data before each test method.
     */
    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(UUID.fromString("84991c79-f6a9-4b7b-b1b4-0d66c0b92c81"))
                .email("test1@gmail.com")
                .password("password1")
                .role(UserRole.USER)
                .build();
    }

    /**
     * Test case for the {@link UserService#save(User)} method.
     */
    @Test
    void saveTest() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        User savedUser = userService.save(user);

        assertThat(savedUser).isNotNull().isEqualTo(user);
        verify(userRepository, times(1)).save(user);
    }

    /**
     * Test case for the {@link UserService#save(User)} method when the user is null.
     */
    @Test
    void saveNullTest() {
        when(userRepository.save(any(User.class))).thenReturn(null);
        assertThatThrownBy(() -> userService.save(null))
                .isInstanceOf(NullUserPropertyException.class);
    }

    /**
     * Test case for the {@link UserService#update(User)} method.
     */
    @Test
    void updateTest() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        User updatedUser = userService.update(user);

        assertThat(updatedUser).isNotNull().isEqualTo(user);
        verify(userRepository, times(1)).save(user);
    }

    /**
     * Test case for the {@link UserService#update(User)} method when the user is null.
     */
    @Test
    void updateNullTest() {
        when(userRepository.save(any(User.class))).thenReturn(null);
        assertThatThrownBy(() -> userService.update(null))
                .isInstanceOf(NullUserPropertyException.class);
    }

    /**
     * Test case for the {@link UserService#updateByEmailDynamically(User, String)} method.
     */
    @Test
    void updateByEmailDynamicallyTest() {
        when(userRepository.updateUserByEmailDynamically(any(User.class), eq(user.getEmail()))).thenReturn(1);
        int updatedRecord = userService.updateByEmailDynamically(user, user.getEmail());

        assertThat(updatedRecord).isEqualTo(1);
        verify(userRepository, times(1)).updateUserByEmailDynamically(user, user.getEmail());
    }

    /**
     * Test case for the {@link UserService#updateByEmailDynamically(User, String)} method when the email is null.
     */
    @Test
    void updateByEmailDynamicallyIsNullTest() {
        assertThatThrownBy(() -> userService.updateByEmailDynamically(user, null))
                .isInstanceOf(NullEmailException.class);
    }

    /**
     * Test case for the {@link UserService#findById(UUID)} method.
     */
    @Test
    void findByIdTest() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        User foundUser = userService.findById(user.getId());

        assertThat(foundUser).isNotNull().isEqualTo(user);
        verify(userRepository, times(1)).findById(user.getId());
    }

    /**
     * Test case for the {@link UserService#findById(UUID)} method when the provided id is null.
     */
    @Test
    void findByIdNullTest() {
        assertThatThrownBy(() -> userService.findById(null))
                .isInstanceOf(NullUserPropertyException.class);
    }
    /**
     * Test case for the {@link UserService#findById(UUID)} method
     * when the user with provided id does not exist.
     */
    @Test
    void findByIdNotFoundTest() {
        UUID nonExistentUserId = UUID.randomUUID();

        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.findById(nonExistentUserId))
                .isInstanceOf(NoUserFoundByIdException.class);
    }

    /**
     * Test case for the {@link UserService#findByEmail(String)} method.
     */
    @Test
    void findByEmailTest() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        User foundUser = userService.findByEmail(user.getEmail());

        assertThat(foundUser).isNotNull().isEqualTo(user);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    /**
     * Test case for the {@link UserService#findByEmail(String)} method when the provided email is null.
     */
    @Test
    void findByEmailNullTest() {
        assertThatThrownBy(() -> userService.findByEmail(null))
                .isInstanceOf(NullUserPropertyException.class);
    }

    /**
     * Test case for the {@link UserService#findByEmail(String)} method
     * when the user with provided email does not exist.
     */
    @Test
    void findByEmailNotFoundTest() {
        String nonExistentUserEmail = "nonexistent@gmail.com";

        when(userRepository.findByEmail(nonExistentUserEmail)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.findByEmail(nonExistentUserEmail))
                .isInstanceOf(NoUserFoundByEmailException.class);
    }

    /**
     * Test case for the {@link UserService#deleteById(UUID)} method.
     */
    @Test
    void deleteByIdTest() {
        userService.deleteById(user.getId());
        verify(userRepository, times(1)).deleteById(user.getId());
    }

    /**
     * Test case for the {@link UserService#deleteById(UUID)} method when the provided id is null.
     */
    @Test
    void deleteByIdNullTest() {
        assertThatThrownBy(() -> userService.deleteById(null))
                .isInstanceOf(NullUserPropertyException.class);
    }
}
