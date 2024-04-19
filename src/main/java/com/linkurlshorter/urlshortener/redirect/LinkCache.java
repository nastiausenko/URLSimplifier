package com.linkurlshorter.urlshortener.redirect;

import com.linkurlshorter.urlshortener.link.Link;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Component class for caching links to optimize redirection performance.
 *
 * <p>This class provides a simple in-memory cache for storing links by their short links.
 * It allows for quick retrieval of links based on short links, reducing the need for repeated
 * database queries during redirection requests.
 *
 * @author Egor Sivenko
 */
@Component
@RequiredArgsConstructor
public class LinkCache {

    private final Map<String, Link> cache = new HashMap<>();

    /**
     * Checks if the cache contains the specified short link.
     *
     * @param shortLink the short link to check for in the cache
     * @return true if the cache contains the short link, false otherwise
     */
    public boolean containsShortLink(String shortLink) {
        return cache.containsKey(shortLink);
    }

    /**
     * Retrieves the link associated with the specified short link from the cache.
     *
     * @param shortLink the short link to retrieve the associated link for
     * @return the link associated with the short link, or null if not found in the cache
     */
    public Link getByShortLink(String shortLink) {
        return cache.get(shortLink);
    }

    /**
     * Puts a new link into the cache with the specified short link.
     *
     * @param shortLink the short link to use as the cache key
     * @param link      the link to be cached
     */
    public void putLink(String shortLink, Link link) {
        cache.put(shortLink, link);
    }
}
