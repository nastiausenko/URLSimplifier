package com.linkurlshorter.urlshortener.link;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Represents a response object for modifying a link.
 * <p>
 * This class encapsulates the response data after attempting to modify a link,
 * including any error messages that may occur during the process.
 * </p>
 *
 * <p>
 * An instance of this class is typically returned from methods that modify links,
 * providing information about the success or failure of the operation.
 * </p>
 *
 * <p>
 * The {@code error} field contains any error message that occurred during the modification process.
 * </p>
 *
 * @author [Author Name]
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkModifyingResponse {
    private String error;
}
