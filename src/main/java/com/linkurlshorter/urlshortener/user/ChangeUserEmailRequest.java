package com.linkurlshorter.urlshortener.user;

import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * Represents a request payload used to change a user's email address.
 *
 * <p>
 * This class encapsulates the new email address to be assigned to the user.
 * </p>
 * @author Artem Poliakov
 * @version 1.0
 */
@Data
@AllArgsConstructor
public class ChangeUserEmailRequest {
    private String newEmail;
}
