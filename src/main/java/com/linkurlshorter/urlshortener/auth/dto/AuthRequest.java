package com.linkurlshorter.urlshortener.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a request object for user authentication.
 *
 * <p>This class encapsulates the email and password fields required for user authentication.
 * It is used as a DTO (Data Transfer Object) for incoming authentication requests.
 *
 * @author Egor Sivenko
 */
@Data
@AllArgsConstructor
public class AuthRequest {

    @NotBlank
    @Size(min = 10, max = 255)
    private String email;

    @NotBlank
    @Size(min = 8, max = 255)
    private String password;
}
