package com.linkurlshorter.urlshortener.auth.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * The EmailValidatorImpl class implements {@link ConstraintValidator} the ConstraintValidator interface to
 * validate email addresses. It checks whether an email address is valid according to a specified regular
 * expression pattern.
 *
 * @author Vlas Pototskyi
 */
public class EmailValidatorImpl implements ConstraintValidator<EmailValidator, String> {
    /**
     * Checks if the specified email address matches the specified regular expression pattern.
     *
     * @param email   The email address to be validated.
     * @param context The ConstraintValidatorContext for applying custom constraints.
     * @return true if the email address is valid otherwise false.
     */
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        String regexEmail = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (email == null || !email.matches(regexEmail)) {
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}

