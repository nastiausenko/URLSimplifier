package com.linkurlshorter.urlshortener.link;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

/**
 * The EndTimeLinkValidatorImpl class implements the {@link ConstraintValidator} interface
 * for annotating an EndTimeLinkValidator. It is used to validate the LocalDateTime value to check
 * that the time to which the link is valid is correct.
 *
 * @author Vlas Pototskyi
 */
public class EndTimeLinkValidatorImpl implements ConstraintValidator<EndTimeLinkValidator, LocalDateTime> {
    /**
     * Checks if the time to which the reference is valid is greater than the current time.
     *
     * @param value   LocalDateTime The value to be validated.
     * @param context The context to validate.
     * @return true if the time to which the reference is valid is greater than the current time; false otherwise.
     */
    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        LocalDateTime currentTime = LocalDateTime.now();
        return value.isAfter(currentTime);
    }
}

