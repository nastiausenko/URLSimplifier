package com.linkurlshorter.urlshortener.link.redirect;

import com.linkurlshorter.urlshortener.link.LinkService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Controller class for handling link redirection requests.
 *
 * <p>This class provides an endpoint for redirecting short links to their corresponding long links.
 *
 * @author Egor Sivenko
 * @see org.springframework.web.servlet.view.RedirectView
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "Link Redirect", description = "The Link Redirect API")
public class LinkRedirectController {

    private final LinkService linkService;

    /**
     * Redirects a request with a short link to its corresponding long link.
     *
     * @param shortLink the short link to be redirected
     * @return a RedirectView object directing the user to the long link
     */
    @SneakyThrows
    @GetMapping("/{shortLink}")
    @ApiResponse(responseCode = "302", description = "Redirect to original link")
    public RedirectView redirectToOriginalLink(@PathVariable @NotBlank @Size(min = 3, max = 20) String shortLink) {
        String longLink = linkService.getLongLinkFromShortLink(shortLink);
        return buildRedirectView(longLink);
    }

    /**
     * Builds RedirectView to the provided url with 302 status code.
     *
     * @param url the url to redirect to
     * @return a RedirectView object directing the user to the url
     */
    private RedirectView buildRedirectView(String url) {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);
        redirectView.setStatusCode(HttpStatusCode.valueOf(302));
        return redirectView;
    }
}
