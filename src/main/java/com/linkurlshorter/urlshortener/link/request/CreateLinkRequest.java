package com.linkurlshorter.urlshortener.link.request;

import com.linkurlshorter.urlshortener.link.validation.UrlLongFormatValidator;
import com.linkurlshorter.urlshortener.link.validation.UrlNewShortValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a request object for creating a link.
 * <p>
 * This class encapsulates the data required to create a link, including the long URL.
 * </p>
 *
 * <p>
 * An instance of this class is typically used as a parameter in methods that create links.
 * </p>
 *
 * <p>
 * The long URL provided in the request is validated using the {@link UrlLongFormatValidator} annotation.
 * </p>
 *
 * @author Artem Poliakov
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateLinkRequest {
    @UrlLongFormatValidator
    private String longLink;
    @UrlNewShortValidator
    private String shortLinkName;
}
