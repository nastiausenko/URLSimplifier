package com.linkurlshorter.urlshortener.security;

import com.linkurlshorter.urlshortener.user.User;
import com.linkurlshorter.urlshortener.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Service class implementing Spring Security's UserDetailsService interface
 * for custom user retrieval during authentication.
 *
 * <p>This class provides the functionality to load user details by email
 * and convert them into a UserDetails object required for authentication and authorization.
 *
 * @author Egor Sivenko
 * @see org.springframework.security.core.userdetails.UserDetailsService
 * @see UserService
 * @see SecurityUser
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Service for managing user-related operations.
     */
    private final UserService userService;

    /**
     * Loads user details by their email address.
     *
     * @param email the email address of the user
     * @return UserDetails representing the loaded user
     */
    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userService.findByEmail(email);
        return new SecurityUser(user);
    }
}
