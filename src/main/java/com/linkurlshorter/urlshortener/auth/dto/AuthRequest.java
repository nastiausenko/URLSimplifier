package com.linkurlshorter.urlshortener.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import com.linkurlshorter.urlshortener.auth.validation.EmailValidator;
import com.linkurlshorter.urlshortener.auth.validation.PasswordValidator;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a request object for user authentication.
 *
 * <p>This class encapsulates the email and password fields required for user authentication.
 * It is used as a DTO (Data Transfer Object) for incoming authentication requests.
 *
 * @author Egor Sivenko, Vlas Pototskyi
 */
@Data
@AllArgsConstructor
public class AuthRequest {

    @EmailValidator
    private String email;

    @PasswordValidator
    private String password;
}
