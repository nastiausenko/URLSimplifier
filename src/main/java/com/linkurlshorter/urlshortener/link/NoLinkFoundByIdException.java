package com.linkurlshorter.urlshortener.link;

/**
 * Exception thrown when no link is found by the provided ID.
 */
public class NoLinkFoundByIdException extends RuntimeException {
    private static final String DEFAULT_MSG = "No link by provided id found";

    public NoLinkFoundByIdException() {
        super(DEFAULT_MSG);
    }

    public NoLinkFoundByIdException(String msg) {
        super(msg);
    }
}
