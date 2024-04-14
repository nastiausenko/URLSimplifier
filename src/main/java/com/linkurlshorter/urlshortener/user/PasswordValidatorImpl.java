package com.linkurlshorter.urlshortener.user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Implementation {@link ConstraintValidator} of a validator to check the password format.
 * Validates password format using a regular expression.
 *
 * @author Vlas Pototskyi
 */

public class PasswordValidatorImpl implements ConstraintValidator<PasswordValidator, String> {
    /**
     * Checks if the entered string matches the password format.
     *
     * @param password String to be validated.
     * @param context  Validation context that can be used to collect errors.
     * @return true if the string is a valid password, false otherwise.
     */
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[^ ]{8,}$";
        if (!password.matches(regex)) {
            context.disableDefaultConstraintViolation();
            return false;
        }
        return true;
    }
}
