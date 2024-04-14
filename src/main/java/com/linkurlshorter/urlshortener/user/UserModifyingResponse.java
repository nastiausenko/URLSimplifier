package com.linkurlshorter.urlshortener.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
/**
 * Represents a response payload for user data altering operations.
 *
 * <p>
 * This class encapsulates information about operation status.
 * </p>
 * @author Artem Poliakov
 * @version 1.0
 */
@Data
@AllArgsConstructor
@Builder
public class UserModifyingResponse {
    private boolean success;
    private String error;
}
