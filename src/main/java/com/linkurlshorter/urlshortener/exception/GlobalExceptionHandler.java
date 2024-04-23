package com.linkurlshorter.urlshortener.exception;

import com.linkurlshorter.urlshortener.auth.exception.EmailAlreadyTakenException;
import com.linkurlshorter.urlshortener.link.*;
import com.linkurlshorter.urlshortener.user.NoSuchEmailFoundException;
import com.linkurlshorter.urlshortener.user.NoUserFoundByEmailException;
import com.linkurlshorter.urlshortener.user.NoUserFoundByIdException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Global exception handler to catch and handle various types of errors throughout the application.
 *
 * @author Vlas Pototskyi
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Handles method argument validation errors and invalid request errors (400).
     * Returns a response with status 400 and the corresponding error message.
     *
     * @param ex method argument validation error
     * @return {@link ResponseEntity} object with the appropriate status and error message
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                Objects.requireNonNull(ex.getFieldError()).getDefaultMessage(),
                request.getRequestURI());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handles AuthenticationException thrown during user authentication.
     *
     * @param ex the AuthenticationException thrown
     * @return {@link ResponseEntity} containing the error response for authentication failure
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(
            AuthenticationException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.UNAUTHORIZED,
                ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    /**
     * Handles EmailAlreadyTakenException thrown during user registration.
     *
     * @param ex the EmailAlreadyTakenException thrown
     * @return {@link ResponseEntity} containing the error response for email already taken
     */
    @ExceptionHandler(EmailAlreadyTakenException.class)
    public ResponseEntity<Object> handleEmailAlreadyTakenException(
            EmailAlreadyTakenException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.BAD_REQUEST,
                ex.getMessage(), request.getRequestURI());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handles no resource (404) exceptions for different types of requests.
     * Returns a response with a 404 status and the corresponding error message.
     *
     * @param ex missing resource exception
     * @return {@link ResponseEntity} object with the corresponding status and error message
     */
    @ExceptionHandler({NoSuchEmailFoundException.class,
            NoUserFoundByEmailException.class, NoUserFoundByIdException.class})
    public ResponseEntity<Object> handleNotFoundExceptions(
            RuntimeException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.NOT_FOUND,
                ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Creates an error response object.
     *
     * @param status     status of the error
     * @param message    error message
     * @param requestURI request URL
     * @return an {@link ErrorResponse} object with the appropriate data
     */
    private ErrorResponse buildErrorResponse(HttpStatus status, String message, String requestURI) {
        return new ErrorResponse(LocalDateTime.now(), status.value(), message, requestURI);
    }

    /**
     * Handles Forbidden (403) exceptions for different types of requests.
     * Returns a response with a 403 status and the corresponding error message.
     *
     * @param ex      forbidden exception
     * @param request HttpServletRequest object representing the HTTP request
     * @return {@link ResponseEntity} object with the corresponding status and error message
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Object> handleForbiddenException(
            ForbiddenException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.FORBIDDEN,
                ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler({NoLinkFoundByIdException.class, NoLinkFoundByShortLinkException.class})
    public ResponseEntity<Object> handleNoLinkFoundByIdException(
            RuntimeException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.NOT_FOUND,
                ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(DeletedLinkException.class)
    public ResponseEntity<Object> handleDeletedLinkException(
            DeletedLinkException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.BAD_REQUEST,
                ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(LinkStatusException.class)
    public ResponseEntity<Object> handleLinkStatusException(
            LinkStatusException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.BAD_REQUEST,
                ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InternalServerLinkException.class)
    public ResponseEntity<Object> handleInternalServerLinkException(
            InternalServerLinkException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}

