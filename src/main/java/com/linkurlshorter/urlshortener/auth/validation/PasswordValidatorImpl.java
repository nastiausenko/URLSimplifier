
package com.linkurlshorter.urlshortener.auth.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Implementation {@link ConstraintValidator} of a validator to check the password format.
 * Validates password format using a regular expression.
 *
 * @author Vlas Pototskyi
 */
public class PasswordValidatorImpl implements ConstraintValidator<PasswordValidator, String> {
    /**
     * Regular expression pattern for validating password format.
     */
    private static final Pattern PASSWORD_PATTERN = Pattern.
            compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[^ ]{8,64}$");

    /**
     * Checks if the entered string matches the password format.
     *
     * @param password String to be validated.
     * @param context  Validation context that can be used to collect errors.
     * @return true if the string is a valid password, false otherwise.
     */
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }
        if (!password.matches(PASSWORD_PATTERN.pattern())) {
            if (context != null) {
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                        .addConstraintViolation();
            }
            return false;
        }
        return true;
    }
}