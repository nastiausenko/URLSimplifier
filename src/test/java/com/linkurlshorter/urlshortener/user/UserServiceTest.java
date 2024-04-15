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

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
       user = User.builder()
                .email("test1@gmail.com")
                .password("password1")
                .role(UserRole.USER)
                .build();
    }

    @Test
    void saveTest() {
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        User savedUser = userService.save(user);

        assertNotNull(savedUser);
        assertEquals(user, savedUser);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateTest() {
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        User updatedUser = userService.update(user);

        assertNotNull(updatedUser);
        assertEquals(user, updatedUser);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateByEmailDynamicallyTest() {
        when(userRepository.updateUserByEmailDynamically(any(User.class), eq(user.getEmail()))).thenReturn(1);
        int updatedRecord = userService.updateByEmailDynamically(user, user.getEmail());

        assertEquals(1, updatedRecord);
        verify(userRepository, times(1)).updateUserByEmailDynamically(user, user.getEmail());
    }

    @Test
    void updateByEmailDynamicallyIsNullTest() {
        assertThrows(NullEmailException.class, () -> {
            userService.updateByEmailDynamically(user, null);
        });
    }

    @Test
    void findByIdTest() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        User foundUser = userService.findById(user.getId());

        assertEquals(user, foundUser);
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void findByIdNotFoundTest() {
        UUID nonExistentUserId = UUID.randomUUID();

        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());
        assertThrows(NoUserFoundByIdException.class, () -> userService.findById(nonExistentUserId));
    }

    @Test
    void findByEmailTest() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        User foundUser = userService.findByEmail(user.getEmail());

        assertEquals(user, foundUser);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void findByEmailNotFoundTest() {
        String nonExistentUserEmail = "nonexistent@gmail.com";

        when(userRepository.findByEmail(nonExistentUserEmail)).thenReturn(Optional.empty());
        assertThrows(NoUserFoundByEmailException.class, () -> userService.findByEmail(nonExistentUserEmail));
    }

    @Test
    void deleteByIdTest() {
        userService.deleteById(user.getId());
        verify(userRepository, Mockito.times(1)).deleteById(user.getId());
    }
}
