package com.linkurlshorter.urlshortener.auth.validation;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * Test class for{@link PasswordValidatorImpl}.
 * This class contains unit tests to verify the functionality of the {@link PasswordValidatorImpl} class.
 *
 * @author Vlas Pototskyi
 */
class PasswordValidatorImplTest {
    private PasswordValidatorImpl passwordValidator;

    /**
     * Set up method to initialize the {@link PasswordValidatorImpl} instance before each test method.
     */
    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidatorImpl();
    }

    /**
     * Test method to verify the validation of a valid password.
     * The password "Hogvards_15F" is considered valid.
     */
    @Test
    void verificationOfValidPassword() {
        assertThat(passwordValidator.isValid("Hogvards_15F", null)).isTrue();
    }

    /**
     * Test method to verify the validation of an invalid password.
     * The password "password" is considered invalid.
     */
    @Test
    void verificationOfInvalidPassword() {
        assertThat(passwordValidator.isValid("password", null)).isFalse();
    }

    /**
     * Test method to verify the behavior when an empty password is provided.
     * An empty password is considered invalid.
     */
    @Test
    void checkingEmptyPassword() {
        assertThat(passwordValidator.isValid(null, null)).isFalse();
    }
}
