package com.linkurlshorter.urlshortener.user;

/**
 * This exception is thrown when no matching email is found in the database.
 *
 * <p>
 * This exception can be thrown in situations where an operation expects
 * to find or alter a user's information based on their email address, but no
 * matching email is found in the database.
 * </p>
 *
 * <p>
 * {@author Artem Poliakov}
 * </p>
 */
public class NoSuchEmailFoundException extends RuntimeException {
    private static final String MSG = "No such email found in the database";

    public NoSuchEmailFoundException() {
        super(MSG);
    }

    public NoSuchEmailFoundException(String msg) {
        super(msg);
    }
}
