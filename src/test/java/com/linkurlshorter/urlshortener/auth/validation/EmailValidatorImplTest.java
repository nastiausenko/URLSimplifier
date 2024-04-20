package com.linkurlshorter.urlshortener.auth.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Test class for {@link EmailValidatorImpl}.
 * This class contains unit tests to verify the functionality of the {@link EmailValidatorImpl} class.
 */
class EmailValidatorImplTest {

    private EmailValidatorImpl emailValidator;

    /**
     * Set up method to initialize the {@link EmailValidatorImpl} instance before each test method.
     */
    @BeforeEach
    void setUp() {
        emailValidator = new EmailValidatorImpl();
    }

    /**
     * Test method to verify the validation of a valid email address.
     * The email address "test@gmail.com" is considered valid.
     */
    @Test
    void verificationOfValidEmail() {
        assertThat(emailValidator.isValid("test@gmail.com", null)).isTrue();
    }

    /**
     * Test method to verify the validation of an invalid email address.
     * The email address "test.gmail.com" is considered invalid.
     */
    @Test
    void verificationOfInvalidEmail() {
        assertThat(emailValidator.isValid("test.gmail.com", null)).isFalse();
    }

    /**
     * Test method to verify the behavior when an empty email address is provided.
     * An empty email address is considered invalid.
     */
    @Test
    void checkingEmptyEmail() {
        assertThat(emailValidator.isValid(null, null)).isFalse();
    }
}
