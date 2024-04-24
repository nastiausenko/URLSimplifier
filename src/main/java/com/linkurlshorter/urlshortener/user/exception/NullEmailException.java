
package com.linkurlshorter.urlshortener.user.exception;

/**
 * This exception is thrown when a null email is provided, indicating that the request cannot be processed.
 *
 * <p>
 * This exception can be thrown in situations where an operation requires a non-null email value,
 * and null is provided instead.
 * </p>
 *
 * <p>
 *
 * @author Artem Poliakov
 * @version 1.0
 * </p>
 */
public class NullEmailException extends NullUserPropertyException {
    private static final String MSG = "Email provided is null, so request can not be processed";

    public NullEmailException() {
        super(MSG);
    }
}