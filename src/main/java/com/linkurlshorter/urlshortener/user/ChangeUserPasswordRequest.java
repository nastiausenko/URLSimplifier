package com.linkurlshorter.urlshortener.user;

import com.linkurlshorter.urlshortener.auth.validation.PasswordValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a request payload used to change a user's password.
 *
 * <p>
 * This class encapsulates the new password to be assigned to the user.
 * </p>
 * @author Artem Poliakov
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserPasswordRequest {
    @PasswordValidator
    private String newPassword;
}
