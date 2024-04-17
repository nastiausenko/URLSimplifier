package com.linkurlshorter.urlshortener.link;

import com.linkurlshorter.urlshortener.user.User;
import com.linkurlshorter.urlshortener.user.UserService;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;
/**
 * Controller for Link-related operations such as create, delete, update and get info + statistics
 * @version 1.0
 * @author Artem Poliakov
 * */
@RestController
@RequiredArgsConstructor
@RequestMapping("/link")
public class LinkController {
    private static final int SHORT_LINK_LIFETIME_IN_DAYS = 30;

    private final LinkService linkService;
    private final UserService userService;
    private final EntityManager entityManager;
    /**
     * Controller method for creating a new link.
     * <p>
     * This method handles POST requests to create a new link. It accepts a valid {@link CreateLinkRequest}
     * in the request body and generates a new short URL for the provided long URL. The short URL is then
     * associated with the authenticated user and stored in the database. Upon successful creation of the link,
     * a {@link CreateLinkResponse} containing the newly generated short URL is returned with a status of 200 (OK).
     * If any errors occur during the process, an {@link InternalServerLinkException} is thrown.
     * </p>
     *
     * @param createRequest the request object containing the long URL to be shortened
     * @return a ResponseEntity containing the response object indicating the success or failure of the link creation,
     * along with the generated short URL
     * @throws InternalServerLinkException if an error occurs during the link creation process
     * @see CreateLinkRequest
     * @see CreateLinkResponse
     * @see InternalServerLinkException
     */
    @PostMapping("/create")
    public ResponseEntity<CreateLinkResponse> createLink(@RequestBody @Valid CreateLinkRequest createRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = userService.findByEmail(authentication.getName()).getId();
        String newShortUrl = RandomStringUtils.randomAlphanumeric(8);
        try {
            linkService.save(
                    Link.builder()
                            .longLink(createRequest.getLongLink())
                            .shortLink(newShortUrl)
                            .expirationTime(LocalDateTime.now().plusDays(SHORT_LINK_LIFETIME_IN_DAYS))
                            .user(entityManager.getReference(User.class, userId))
                            .build()  //TODO: add validations (short link being unique etc)
            );
        } catch(Exception e){
            throw new InternalServerLinkException();
        }
        return ResponseEntity.ok(new CreateLinkResponse("ok", newShortUrl));
    }
}
