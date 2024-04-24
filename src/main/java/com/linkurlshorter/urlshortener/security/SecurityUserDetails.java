package com.linkurlshorter.urlshortener.security;

import com.linkurlshorter.urlshortener.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Represents a custom implementation of Spring Security's UserDetails interface,
 * tailored for the application's user entity.
 *
 * <p>This class encapsulates user details required by Spring Security for authentication
 * and authorization purposes.
 *
 * @author Egor Sivenko
 * @see org.springframework.security.core.userdetails.UserDetails
 * @see User
 */
@RequiredArgsConstructor
public class SecurityUserDetails implements UserDetails {

    /**
     * The user entity associated with this SecurityUser instance.
     */
    private final User user;

    /**
     * Returns the username (email) of the user.
     *
     * @return the email address of the user
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Returns the password of the user.
     *
     * @return the password of the user
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Returns the authorities granted to the user.
     *
     * @return a collection of authorities granted to the user
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(
                new SimpleGrantedAuthority(user.getRole().toString())
        );
    }

    /**
     * Indicates whether the user's account has expired. An expired account cannot be
     * authenticated.
     *
     * @return always returns true as account expiration is not considered
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked. A locked user cannot be
     * authenticated.
     *
     * @return always returns true as account locking is not considered
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) has expired. Expired
     * credentials prevent authentication.
     *
     * @return always returns true as credential expiration is not considered
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled. A disabled user cannot be
     * authenticated.
     *
     * @return always returns true as user enabling/disabling is not considered
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
