package com.linkurlshorter.urlshortener.user;

import lombok.AllArgsConstructor;
import lombok.Data;
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
public class ChangeUserPasswordRequest {
    private String newPassword;
}
