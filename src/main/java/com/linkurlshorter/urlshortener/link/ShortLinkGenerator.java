package com.linkurlshorter.urlshortener.link;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
public class ShortLinkGenerator {

    public String generate() {
        return RandomStringUtils.randomAlphanumeric(8);
    }
}
