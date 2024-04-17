package com.linkurlshorter.urlshortener.redirect;

import com.linkurlshorter.urlshortener.link.Link;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LinkCache {

    private final Map<String, Link> cache = new HashMap<>();

    public boolean containsShortLink(String shortLink) {
        return cache.containsKey(shortLink);
    }

    public Link getByShortLink(String shortLink) {
        return cache.get(shortLink);
    }

    public void putLink(String shortLink, Link link) {
        cache.put(shortLink, link);
    }
}
