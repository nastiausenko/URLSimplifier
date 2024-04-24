package com.linkurlshorter.urlshortener.security;

import com.linkurlshorter.urlshortener.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
 * @see UserRepository
 * @see SecurityUserDetails
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Repository for managing user-related operations.
     */
    private final UserRepository userRepository;

    /**
     * Loads user details by their email address.
     *
     * @param email the email address of the user
     * @return UserDetails representing the loaded user
     */
    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository
                .findByEmail(email)
                .map(SecurityUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }
}
