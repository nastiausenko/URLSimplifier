package com.linkurlshorter.urlshortener.link;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LinkStatisticsDto {
    private UUID id;
    private String shortLink;
    private int usageStatistics;
}
