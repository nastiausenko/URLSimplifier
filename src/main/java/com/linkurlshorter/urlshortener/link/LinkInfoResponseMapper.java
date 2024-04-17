package com.linkurlshorter.urlshortener.link;

import org.springframework.stereotype.Component;

/**
 * Mapper class responsible for mapping a Link entity to a LinkInfoResponse object.
 * <p>
 * This mapper class provides a method to map a Link entity along with an optional error message
 * <p>
 * to a LinkInfoResponse object.
 *
 * @author Artem Poliakov
 * @version 1.0
 */
@Component
public class LinkInfoResponseMapper {
    public LinkInfoResponse mapLinkToResponse(Link link, String error) {
        return LinkInfoResponse.builder()
                .id(link.getId())
                .longLink(link.getLongLink())
                .shortLink(link.getShortLink())
                .createdTime(link.getCreatedTime())
                .expirationTime(link.getExpirationTime())
                .usageStatistics(link.getStatistics())
                .status(link.getStatus())
                .error(error)
                .build();
    }
}
