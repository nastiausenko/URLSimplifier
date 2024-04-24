package com.linkurlshorter.urlshortener.user.responce;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a response payload for user data altering operations.
 *
 * <p>
 * This class encapsulates information about operation status.
 * </p>
 *
 * @author Artem Poliakov
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserModifyingResponse {
    private String error;
    private String jwtToken;
}