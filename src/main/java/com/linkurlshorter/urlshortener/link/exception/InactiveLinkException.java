package com.linkurlshorter.urlshortener.link.exception;

public class InactiveLinkException extends RuntimeException {

    public static final String MESSAGE = "Link '%s' is currently inactive and cannot be redirected";

    public InactiveLinkException(String link) {
        super(String.format(MESSAGE, link));
    }
}
