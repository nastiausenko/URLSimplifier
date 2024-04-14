package com.linkurlshorter.urlshortener.user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailValidatorImpl implements ConstraintValidator<EmailValidator, String> {
    private final UserService userService;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (!isEmailValid(email)) {
            context.disableDefaultConstraintViolation();
            return false;
        }
        return isUserEmailUnique(email, context);
    }

    private boolean isEmailValid(String email) {
        String regexEmail = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(regexEmail);
    }

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

