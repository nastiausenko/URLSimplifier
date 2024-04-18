package com.linkurlshorter.urlshortener.link;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
/**
 * Data transfer object (DTO) for representing link statistics.
 * This class encapsulates information about a link's ID, short link, and usage statistics.
 *
 * @author Artem Poliakov
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LinkStatisticsDto {
    private UUID id;
    private String shortLink;
    private int usageStatistics;
}
