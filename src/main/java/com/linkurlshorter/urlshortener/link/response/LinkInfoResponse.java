package com.linkurlshorter.urlshortener.link.response;

import com.linkurlshorter.urlshortener.link.dto.LinkInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * Data transfer object (DTO) for representing a response containing link information.
 * This class encapsulates a list of {@link LinkInfoDto} objects representing link information,
 * along with an optional error message.
 *
 * @author Artem Poliakov
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LinkInfoResponse {
    private List<LinkInfoDto> linkDtoList;
    private String error;
}
