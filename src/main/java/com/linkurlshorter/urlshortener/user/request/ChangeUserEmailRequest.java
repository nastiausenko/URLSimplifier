package com.linkurlshorter.urlshortener.user.request;

import com.linkurlshorter.urlshortener.auth.validation.EmailValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a request payload used to change a user's email address.
 *
 * <p>
 * This class encapsulates the new email address to be assigned to the user.
 * </p>
 *
 * @author Artem Poliakov
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserEmailRequest {
    @EmailValidator
    private String newEmail;
}
