package com.linkurlshorter.urlshortener.link;

import com.linkurlshorter.urlshortener.user.ShortenedLinkOwnerValidation;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Represents a request to edit the content of a link.
 * <p>
 * <p>
 * This class encapsulates the data required to modify the content of a link,
 * including the link's ID and the new short link to be assigned.
 * </p>
 *
 * @author Artem Poliakov
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditLinkContentRequest {
    @ShortenedLinkOwnerValidation
    private String oldShortLink;
    @NotNull
    @UrlNewShortValidator
    private String newShortLink;
}
