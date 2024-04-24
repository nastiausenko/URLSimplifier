package com.linkurlshorter.urlshortener.link.validation;

import com.linkurlshorter.urlshortener.link.model.Link;
import com.linkurlshorter.urlshortener.link.LinkService;
import com.linkurlshorter.urlshortener.link.model.LinkStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

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
        if (shortLink == null || shortLink.isEmpty()) {
            context.buildConstraintViolationWithTemplate("Invalid short link!")
                    .addConstraintViolation();
            return false;
        }
        Link link = linkService.findByShortLink(shortLink);
        if (link.getStatus() == LinkStatus.INACTIVE) {
            context.buildConstraintViolationWithTemplate("This link is inactive!")
                    .addConstraintViolation();
            return false;
        }

        if (linkService.doesLinkExist(shortLink)) {
            context.buildConstraintViolationWithTemplate("This link already exists!")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}