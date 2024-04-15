package com.linkurlshorter.urlshortener.auth.dto;

/**
 * Represents a response object for user authentication.
 *
 * <p>This record encapsulates a message and a JWT token returned after successful authentication.
 * It is used as a DTO (Data Transfer Object) for authentication responses.
 *
 * @author Egor Sivenko
 */
public record AuthResponse(
        String message,
        String jwtToken
) {}
