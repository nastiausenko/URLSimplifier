package com.linkurlshorter.urlshortener.link.generator;

import com.linkurlshorter.urlshortener.link.LinkService;
import com.linkurlshorter.urlshortener.link.exception.InternalServerLinkException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShortLinkGenerator {
    private final LinkService linkService;

    public String generate() {
        for (int i = 0; i < 5; i++) {
            String newShortLink = RandomStringUtils.randomAlphanumeric(8);
            if (!linkService.doesLinkExist(newShortLink)) {
                return newShortLink;
            }
        }
        throw new InternalServerLinkException();
    }
}
