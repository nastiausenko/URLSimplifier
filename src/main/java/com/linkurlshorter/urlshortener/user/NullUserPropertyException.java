package com.linkurlshorter.urlshortener.user;

/**
 * Exception thrown when a user entity or a required user property is found to be null,
 * making the request unable to be processed.
 *
 * @author Artem Poliakov
 * @version 1.0
 */
public class NullUserPropertyException extends RuntimeException {
    private static final String MSG = "User entity or required user property is null, so request can not be processed";

    public NullUserPropertyException() {
        super(MSG);
    }

    public NullUserPropertyException(String msg) {
        super(msg);
    }
}
