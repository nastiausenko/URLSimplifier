package com.linkurlshorter.urlshortener.redirect;

import com.linkurlshorter.urlshortener.link.Link;
import com.linkurlshorter.urlshortener.link.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;

/**
 * Controller class for handling link redirection requests.
 *
 * <p>This class provides an endpoint for redirecting short links to their corresponding long links.
 * It first checks if the short link is cached in {@link LinkCache}. If the short link is found in the cache,
 * it retrieves the link directly from the cache; otherwise, it queries the {@link LinkService} to fetch
 * the link from the database. After retrieving the link, it updates its statistics and expiration time,
 * caches the link for future requests and redirects the user to the corresponding long link.
 *
 * @author Egor Sivenko
 * @see org.springframework.web.servlet.view.RedirectView
 */
@RestController
@RequiredArgsConstructor
public class LinkRedirectController {

    private final LinkCache linkCache;
    private final LinkService linkService;

    /**
     * Redirects a request with a short link to its corresponding long link.
     *
     * @param shortLink the short link to be redirected
     * @return a RedirectView object directing the user to the long link
     */
    @GetMapping("/{shortLink}")
    public RedirectView redirectToOriginalLink(@PathVariable("shortLink") String shortLink) {
        Link link = linkCache.containsShortLink(shortLink)
                ? linkCache.getByShortLink(shortLink)
                : linkService.findByShortLink(shortLink);

        updateLinkStats(link);
        return redirectToLongLink(link);
    }

    /**
     * Updates the link statistics, expiration time, and caches the link.
     *
     * @param link the link to be updated
     */
    private void updateLinkStats(Link link) {
        link.setStatistics(link.getStatistics() + 1);
        link.setExpirationTime(LocalDateTime.now().plusMonths(1));

        linkService.save(link);
        linkCache.putLink(link.getShortLink(), link);
    }

    /**
     * Redirects to the long link.
     *
     * @param link the link to redirect to
     * @return a RedirectView object directing the user to the long link
     */
    private RedirectView redirectToLongLink(Link link) {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(link.getLongLink());
        redirectView.setStatusCode(HttpStatusCode.valueOf(302));
        return redirectView;
    }
}
