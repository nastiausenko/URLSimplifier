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

@RestController
@RequiredArgsConstructor
public class LinkRedirectController {

    private final LinkService linkService;

    @GetMapping("/{shortLink}")
    public RedirectView redirectToLongLink(@PathVariable("shortLink") String shortLink) {
        Link link = linkService.findByShortLink(shortLink);
        link.setStatistics(link.getStatistics() + 1);
        link.setExpirationTime(LocalDateTime.now().plusMonths(1));
        linkService.save(link);

        String longLink = link.getLongLink();

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(longLink);
        redirectView.setStatusCode(HttpStatusCode.valueOf(302));
        return redirectView;
    }
}
