package com.linkurlshorter.urlshortener.user.validation;

import com.linkurlshorter.urlshortener.link.LinkService;
import com.linkurlshorter.urlshortener.user.UserService;
import com.linkurlshorter.urlshortener.user.validation.ShortenedLinkOwnerValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.UUID;

/**
 * Implementation of user ownership verification for a shortened link.
 * The check is performed using the link service {@link LinkService} and the user service {@link UserService}.
 */
@RequiredArgsConstructor
public class ShortenedLinkOwnerValidationImpl implements ConstraintValidator<ShortenedLinkOwnerValidation, String> {
    private final LinkService linkService;
    private final UserService userService;

    /**
     * Checks user ownership of the shortened link.
     *
     * @param shortLink The shortened link to check ownership for.
     * @param context   The context of the constraint check.
     * @return true if the current user owns the link; false otherwise.
     */
    @Override
    public boolean isValid(String shortLink, ConstraintValidatorContext context) {
        if (shortLink == null || shortLink.isEmpty()) {
            context.buildConstraintViolationWithTemplate("Invalid short link!")
                    .addConstraintViolation();
            return false;
        }
        UUID currentUserId = getCurrentUserId();
        UUID linkUserId = linkService.findByShortLink(shortLink).getUser().getId();
        if (!Objects.equals(currentUserId, linkUserId)) {
            context.buildConstraintViolationWithTemplate("You cannot do this!")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    /**
     * Gets the ID of the current user.
     *
     * @return The ID of the current user.
     */
    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return userService.findByEmail(userEmail).getId();
    }
}
