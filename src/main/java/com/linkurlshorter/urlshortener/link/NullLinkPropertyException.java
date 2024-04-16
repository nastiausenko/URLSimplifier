package com.linkurlshorter.urlshortener.link;
/**
 * This exception class represents a situation where a required property or link entity is null,
 * resulting in a failed operation.
 * <p>
 * This exception is typically thrown when a critical property or entity required for an operation
 * is unexpectedly null.
 * @version 1.0
 */
public class NullLinkPropertyException extends RuntimeException {
    private static final String DEFAULT_MSG = "Some required link property or link entity was null, operation failed";

    public NullLinkPropertyException() {
        super(DEFAULT_MSG);
    }

    public NullLinkPropertyException(String msg) {
        super(msg);
    }
}
