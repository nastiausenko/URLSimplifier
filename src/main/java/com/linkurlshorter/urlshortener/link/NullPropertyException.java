package com.linkurlshorter.urlshortener.link;
/**
 * This exception class represents a situation where a required property or link entity is null,
 * resulting in a failed operation.
 * <p>
 * This exception is typically thrown when a critical property or entity required for an operation
 * is unexpectedly null.
 * @version 1.0
 */
public class NullPropertyException extends RuntimeException {
    private static final String DEFAULT_MSG = "Some required property or link entity was null, operation failed";

    public NullPropertyException() {
        super(DEFAULT_MSG);
    }

    public NullPropertyException(String msg) {
        super(msg);
    }
}
