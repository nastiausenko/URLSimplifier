package com.linkurlshorter.urlshortener.user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

/**
 * The EmailValidatorImpl class implements {@link ConstraintValidator} the ConstraintValidator interface to
 * validate email addresses. It checks whether an email address is valid according to a specified regular
 * expression pattern and verifies if the email address is unique within the system by querying a UserService.
 *
 * @author Vlas Pototskyi
 */
@RequiredArgsConstructor
public class EmailValidatorImpl implements ConstraintValidator<EmailValidator, String> {

    private final UserService userService;

    /**
     * Validates the provided email address.
     *
     * @param email   The email address to be validated.
     * @param context The ConstraintValidatorContext for applying custom constraints.
     * @return true if the email address is valid and unique; otherwise, false.
     */
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (!isEmailValid(email)) {
            context.disableDefaultConstraintViolation();
            return false;
        }
        return isUserEmailUnique(email, context);
    }

    /**
     * Checks whether the provided email address matches a specified regular expression pattern.
     *
     * @param email The email address to be validated.
     * @return true if the email address matches the pattern; otherwise, false.
     */
    private boolean isEmailValid(String email) {
        String regexEmail = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(regexEmail);
    }

    /**
     * Checks if the provided email address is unique within the system.
     *
     * @param email   The email address to be validated.
     * @param context The ConstraintValidatorContext for applying custom constraints.
     * @return true if the email address is unique; otherwise, false.
     */
    private boolean isUserEmailUnique(String email, ConstraintValidatorContext context) {
        try {
            userService.findByEmail(email);
            context.buildConstraintViolationWithTemplate("User email is already exist!").addConstraintViolation();
            context.disableDefaultConstraintViolation();
            return false;
        } catch (NoUserFoundByEmailException ex) {
            return true;
        }
    }
}

