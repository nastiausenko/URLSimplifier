package com.linkurlshorter.urlshortener.auth.dto;

import com.linkurlshorter.urlshortener.auth.validation.EmailValidator;
import com.linkurlshorter.urlshortener.auth.validation.PasswordValidator;
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
 * @author Vlas Pototskyi
 * @author Anastasiia Usenko
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

    @EmailValidator
    private String email;

    @PasswordValidator
    private String password;
}
