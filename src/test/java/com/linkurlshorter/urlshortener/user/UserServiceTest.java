package com.linkurlshorter.urlshortener.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        User savedUser = userService.save(user);

        assertNotNull(savedUser);
        assertEquals(user, savedUser);
        verify(userRepository, times(1)).save(user);
    }

    /**
     * Test case for the {@link UserService#update(User)} method.
     */
    @Test
    void updateTest() {
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        User updatedUser = userService.update(user);

        assertNotNull(updatedUser);
        assertEquals(user, updatedUser);
        verify(userRepository, times(1)).save(user);
    }

    /**
     * Test case for the {@link UserService#updateByEmailDynamically(User, String)} method.
     */
    @Test
    void updateByEmailDynamicallyTest() {
        when(userRepository.updateUserByEmailDynamically(any(User.class), eq(user.getEmail()))).thenReturn(1);
        int updatedRecord = userService.updateByEmailDynamically(user, user.getEmail());

        assertEquals(1, updatedRecord);
        verify(userRepository, times(1)).updateUserByEmailDynamically(user, user.getEmail());
    }

    /**
     * Test case for the {@link UserService#updateByEmailDynamically(User, String)} method when the email is null.
     */
    @Test
    void updateByEmailDynamicallyIsNullTest() {
        assertThrows(NullEmailException.class, () -> {
            userService.updateByEmailDynamically(user, null);
        });
    }

    /**
     * Test case for the {@link UserService#findById(UUID)} method.
     */
    @Test
    void findByIdTest() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        User foundUser = userService.findById(user.getId());

        assertEquals(user, foundUser);
        verify(userRepository, times(1)).findById(user.getId());
    }

    /**
     * Test case for the {@link UserService#findById(UUID)} method
     * when the user with provided id does not exist.
     */
    @Test
    void findByIdNotFoundTest() {
        UUID nonExistentUserId = UUID.randomUUID();

        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());
        assertThrows(NoUserFoundByIdException.class, () -> userService.findById(nonExistentUserId));
    }

    /**
     * Test case for the {@link UserService#findByEmail(String)} method.
     */
    @Test
    void findByEmailTest() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        User foundUser = userService.findByEmail(user.getEmail());

        assertEquals(user, foundUser);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    /**
     * Test case for the {@link UserService#findByEmail(String)} method
     * when the user with provided email does not exist.
     */
    @Test
    void findByEmailNotFoundTest() {
        String nonExistentUserEmail = "nonexistent@gmail.com";

        when(userRepository.findByEmail(nonExistentUserEmail)).thenReturn(Optional.empty());
        assertThrows(NoUserFoundByEmailException.class, () -> userService.findByEmail(nonExistentUserEmail));
    }

    /**
     * Test case for the {@link UserService#deleteById(UUID)} method.
     */
    @Test
    void deleteByIdTest() {
        userService.deleteById(user.getId());
        verify(userRepository, Mockito.times(1)).deleteById(user.getId());
    }
}
