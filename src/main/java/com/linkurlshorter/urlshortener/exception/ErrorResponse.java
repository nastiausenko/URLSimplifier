package com.linkurlshorter.urlshortener.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
/**
 * Class representing the error response object.
 *
 * @author Vlas Pototskyi
 */
@Data
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime localDateTime;
    private String message;
    private int statusCode;
    private String exceptionMessage;
}
