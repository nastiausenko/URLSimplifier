package com.linkurlshorter.urlshortener.link;

/**
 * Indicating unexpected exceptions which do not depend on user
 *
 * @author Artem Poliakov
 * @version 1.0
 */
public class InternalServerLinkException extends RuntimeException {
    private static final String DEFAULT_MSG = "Unexpected server error occurred";

    public InternalServerLinkException() {
        super(DEFAULT_MSG);
    }

    public InternalServerLinkException(String msg) {
        super(msg);
    }
}
