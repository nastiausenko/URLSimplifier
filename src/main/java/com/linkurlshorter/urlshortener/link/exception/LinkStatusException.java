package com.linkurlshorter.urlshortener.link.exception;

/**
 * Exception thrown when the status of a link is invalid for a particular operation.
 * <p>
 * This exception is thrown to indicate that the status of a link is not suitable for the intended operation.
 * <p>
 * For example, trying to edit the content of a link with a status other than ACTIVE may result in this exception.
 *
 * @author Artem Poliakov
 * @version 1.0
 */
public class LinkStatusException extends RuntimeException {
    private static final String MSG = "Link status is invalid for the operation";

    public LinkStatusException() {
        super(MSG);
    }
}
