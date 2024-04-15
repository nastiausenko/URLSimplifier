package com.linkurlshorter.urlshortener.auth.exception;

/**
 * Exception thrown when attempting to register a user with an email that is already taken.
 *
 * <p>This exception is thrown to indicate that the provided email address is already registered
 * in the system and cannot be used for registration again.
 *
 * @author Egor Sivenko
 * @see java.lang.RuntimeException
 */
public class EmailAlreadyTakenException extends RuntimeException {

    /**
     * Error message template for the exception.
     */
    private static final String MESSAGE = "Email is already taken: %s";

    /**
     * Constructs a new EmailAlreadyTakenException with the specified email.
     *
     * @param email the email address that is already taken
     */
    public EmailAlreadyTakenException(String email) {
        super(String.format(MESSAGE, email));
    }
}
