package com.linkurlshorter.urlshortener.link;

/**
 * Exception thrown when attempting to operate with a link marked as deleted
 * @version 1.0
 */
public class DeletedLinkException extends RuntimeException {
    private static final String DEFAULT_MSG = "The link has already been deleted, no operations are allowed";

    public DeletedLinkException() {
        super(DEFAULT_MSG);
    }

    public DeletedLinkException(String msg) {
        super(msg);
    }
}
