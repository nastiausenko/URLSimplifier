package com.linkurlshorter.urlshortener.auth.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * The EmailValidatorImpl class implements {@link ConstraintValidator} the ConstraintValidator interface to
 * validate email addresses. It checks whether an email address is valid according to a specified regular
 * expression pattern.
 *
 * @author Vlas Pototskyi
 */
public class EmailValidatorImpl implements ConstraintValidator<EmailValidator, String> {
    /**
     * Regular expression pattern for validating email addresses.
     */
    private static final Pattern EMAIL_PATTERN = Pattern.
            compile("^[A-Za-z0-9]+[._+-]?[A-Za-z0-9]+@[A-Za-z0-9]+[._-]?[A-Za-z0-9]+\\.[A-Za-z]{2,}$");

    /**
     * Checks if the specified email address matches the specified regular expression pattern.
     *
     * @param email   The email address to be validated.
     * @param context The ConstraintValidatorContext for applying custom constraints.
     * @return true if the email address is valid otherwise false.
     */
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return false;
        }
        if (!email.matches(EMAIL_PATTERN.pattern())) {
            if (context != null) {
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                        .addConstraintViolation();
            }
            return false;
        }
        return true;
    }
}