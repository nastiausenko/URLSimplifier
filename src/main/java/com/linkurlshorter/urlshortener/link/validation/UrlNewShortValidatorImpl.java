package com.linkurlshorter.urlshortener.link.validation;

import com.linkurlshorter.urlshortener.link.LinkService;
import com.linkurlshorter.urlshortener.link.validation.UrlNewShortValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * The UrlShortValidatorImpl class implements the {@link ConstraintValidator} interface for annotating an UrlShortValidator.
 * Used to validate short links to ensure that they are unique and their length does not exceed the set maximum
 *
 * @author Vlas Pototskyi
 */
@RequiredArgsConstructor
public class UrlNewShortValidatorImpl implements ConstraintValidator<UrlNewShortValidator, String> {
    private static final int MAX_LINK_LENGTH = 20;
    private static final int MIN_LINK_LENGTH = 3;
    private static final String LINK_SIZE_BOUNDS_EXCEEDED = "Link size is invalid!";
    private static final String ALREADY_EXISTS_MSG = "This link already exists!";
    private static final String INVALID_SHORT_LINK_FORMAT_MSG = "Invalid short link format!";

    private final LinkService linkService;

    /**
     * Checks if the short link is unique and active.
     *
     * @param shortLink A string representing the short link to be validated.
     * @param context   The context to be validated.
     * @return true if the short link is unique, does not exceed size bounds and is alphanumetric only; false otherwise.
     * @throws NullPointerException if the link is not found in the database.
     */
    @Override
    public boolean isValid(String shortLink, ConstraintValidatorContext context) {
        if (shortLink != null) {

            if (!StringUtils.isAlphanumeric(shortLink)) {
                context.buildConstraintViolationWithTemplate(INVALID_SHORT_LINK_FORMAT_MSG)
                        .addConstraintViolation();
                return false;
            }

            if (shortLink.length() > MAX_LINK_LENGTH || shortLink.length() < MIN_LINK_LENGTH) {
                context.buildConstraintViolationWithTemplate(LINK_SIZE_BOUNDS_EXCEEDED)
                        .addConstraintViolation();
                return false;
            }

            if (linkService.doesLinkExist(shortLink)) {
                context.buildConstraintViolationWithTemplate(ALREADY_EXISTS_MSG)
                        .addConstraintViolation();
                return false;
            }

        }
        return true;
    }
}

