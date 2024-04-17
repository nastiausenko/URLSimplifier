package com.linkurlshorter.urlshortener.link;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A response class representing the result of a link creation request.
 * It contains information about any error that occurred during the creation process
 * and the generated short link if the creation was successful.
 *
 * @author Artem Poliakov
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateLinkResponse {
    private String error;
    private String shortLink;
}
