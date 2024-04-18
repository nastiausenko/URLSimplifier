package com.linkurlshorter.urlshortener.link;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

/**
 * The UrlShortValidatorImpl class implements the {@link ConstraintValidator} interface for annotating an UrlShortValidator.
 * Used to validate short links to ensure that they are unique and active.
 *
 * @author Vlas Pototskyi
 */
@RequiredArgsConstructor
public class UrlShortValidatorImpl implements ConstraintValidator<UrlShortValidator, String> {
    private final LinkService linkService;

    /**
     * Checks if the short link is unique and active.
     *
     * @param shortLink A string representing the short link to be validated.
     * @param context   The context to be validated.
     * @return true if the short link is unique and active; false otherwise.
     * @throws NullPointerException if the link is not found in the database.
     */
    @Override
    public boolean isValid(String shortLink, ConstraintValidatorContext context) {
        Link link = linkService.findByShortLink(shortLink);
        Objects.requireNonNull(link, "This link cannot be null!");

        if (link.getStatus() == LinkStatus.INACTIVE) {
            context.buildConstraintViolationWithTemplate("This link is inactive!")
                    .addConstraintViolation();
            return false;
        }

        Link existLink = linkService.findByExistUniqueLink(shortLink);
        if (existLink != null) {
            context.buildConstraintViolationWithTemplate("This link already exists!")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}