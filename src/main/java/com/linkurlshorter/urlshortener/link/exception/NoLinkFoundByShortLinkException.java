package com.linkurlshorter.urlshortener.link.exception;

/**
 * Exception thrown when no link is found by the provided shortLink.
 */
public class NoLinkFoundByShortLinkException extends RuntimeException {
    private static final String DEFAULT_MSG = "No link by provided short link found";

    public NoLinkFoundByShortLinkException() {
        super(DEFAULT_MSG);
    }
}
