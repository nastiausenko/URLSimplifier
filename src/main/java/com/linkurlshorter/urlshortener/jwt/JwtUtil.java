package com.linkurlshorter.urlshortener.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Utility class for JWT (JSON Web Token) generation and parsing.
 *
 * <p>This class provides methods for generating JWT tokens based on authentication information,
 * as well as extracting email from a JWT token.
 *
 * @author Egor Sivenko
 * @see javax.crypto.SecretKey
 * @see io.jsonwebtoken.Jwts
 * @see org.springframework.security.core.Authentication
 */
@Component
public class JwtUtil {

    /**
     * Expiration time for JWT tokens in milliseconds.
     */
    private static final int EXPIRATION_TIME = 30 * 60 * 1000;

    /**
     * Secret key for signing JWT tokens.
     */
    private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

    /**
     * Generates a JWT token based on the provided authentication.
     *
     * @param authentication the authentication object containing user details
     * @return the generated JWT token
     */
    public String generateToken(Authentication authentication) {
        return Jwts.builder()
                .subject(authentication.getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * Extracts the email from a JWT token.
     *
     * @param token the JWT token
     * @return the email extracted from the token
     */
    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
