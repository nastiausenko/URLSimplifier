package com.linkurlshorter.urlshortener.link;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * Data transfer object (DTO) for representing a response containing statistics of links.
 * This class encapsulates a list of {@link LinkStatisticsDto} objects representing link statistics,
 * along with an optional error message.
 *
 * @author Artem Poliakov
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LinkStatisticsResponse {
    private List<LinkStatisticsDto> linksStatsList;
    private String error;
}
