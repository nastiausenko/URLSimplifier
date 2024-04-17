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
 * When a request is made with a short link, the controller fetches the corresponding long link
 * from the database using the {@link LinkService#findByShortLink(String)}, updates link statistics,
 * and redirects the user to the long link.
 * Additionally, it updates the link's expiration time to extend its validity.
 *
 * @author Egor Sivenko
 * @see org.springframework.web.servlet.view.RedirectView
 */
@RestController
@RequiredArgsConstructor
public class LinkRedirectController {

    /**
     * The service responsible for managing links
     */
    private final LinkService linkService;

    private final LinkCache linkCache;

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

    private void updateLinkStats(Link link) {
        link.setStatistics(link.getStatistics() + 1);
        link.setExpirationTime(LocalDateTime.now().plusMonths(1));

        linkService.update(link);
        linkCache.putLink(link.getShortLink(), link);
    }

    private RedirectView redirectToLongLink(Link link) {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(link.getLongLink());
        redirectView.setStatusCode(HttpStatusCode.valueOf(302));
        return redirectView;
    }
}
