package com.linkurlshorter.urlshortener.link.dto;

import com.linkurlshorter.urlshortener.link.model.Link;
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
public class LinkInfoDtoMapper {
    public LinkInfoDto mapLinkToDto(Link link) {
        return LinkInfoDto.builder()
                .id(link.getId())
                .longLink(link.getLongLink())
                .shortLink(link.getShortLink())
                .createdTime(link.getCreatedTime())
                .expirationTime(link.getExpirationTime())
                .usageStatistics(link.getStatistics())
                .status(link.getStatus())
                .build();
    }
}
