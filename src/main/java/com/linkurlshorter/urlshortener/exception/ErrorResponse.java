package com.linkurlshorter.urlshortener.exception;

import java.time.LocalDateTime;

/**
 * Class representing the error response object.
 *
 * @author Vlas Pototskyi
 */
public record ErrorResponse(
        LocalDateTime dateTime,
        int statusCode,
        String message,
        String exceptionMessage
) {}
