package com.linkurlshorter.urlshortener.link;

import com.linkurlshorter.urlshortener.security.ForbiddenException;
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
 *
 * @author Artem Poliakov
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/link")
public class LinkController {
    private static final int SHORT_LINK_LIFETIME_IN_DAYS = 30;
    private static final String OPERATION_FORBIDDEN_MSG = "Operation forbidden!";

    private final LinkService linkService;
    private final UserService userService;
    private final EntityManager entityManager;
    private final LinkInfoResponseMapper infoResponseMapper;

    private final LinkInfoResponseMapper infoResponseMapper;

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
    public ResponseEntity<CreateLinkResponse> createLink(@RequestBody @Valid CreateLinkRequest createRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = userService.findByEmail(authentication.getName()).getId();
        String newShortUrl = generateShortLink();
        try {
            linkService.save(
                    Link.builder()
                            .longLink(createRequest.getLongLink())
                            .shortLink(newShortUrl)
                            .expirationTime(LocalDateTime.now().plusDays(SHORT_LINK_LIFETIME_IN_DAYS))
                            .user(entityManager.getReference(User.class, userId))
                            .build()  //TODO: add validations (short link being unique etc)
            );
        } catch (Exception e) {
            throw new InternalServerLinkException();
        }
        return ResponseEntity.ok(new CreateLinkResponse("ok", newShortUrl));
    }

    /**
     * Handles a request to delete a link.
     *
     * @param id the UUID of the link to delete
     * @return a ResponseEntity containing the response object indicating the success of the deletion operation
     * @throws ForbiddenException if the authenticated user does not have rights to delete the link
     */
    @PostMapping("/delete")
    public ResponseEntity<LinkModifyingResponse> deleteLink(@RequestParam UUID id) {
        if (doesUserHaveRightsForLinkById(id)) {
            linkService.deleteById(id);
            return ResponseEntity.ok(new LinkModifyingResponse("ok"));
        } else {
            throw new ForbiddenException(OPERATION_FORBIDDEN_MSG);
        }
    }

    /**
     * Handles a request to edit the content of a link.
     *
     * @param request the request object containing the ID of the link and the new short link
     * @return a ResponseEntity containing the response object indicating the success of the edit operation
     * @throws ForbiddenException  if the authenticated user does not have rights to edit the link
     * @throws LinkStatusException if the status of the link is not ACTIVE
     */
    @PostMapping("/edit/content")
    public ResponseEntity<LinkModifyingResponse> editLinkContent(@RequestBody EditLinkContentRequest request) {
        if (doesUserHaveRightsForLinkById(request.getId())) {
            Link oldLink = linkService.findById(request.getId());
            if (oldLink.getStatus() != LinkStatus.ACTIVE) {
                throw new LinkStatusException();
            }
            oldLink.setShortLink(request.getNewShortLink());    //TODO: add short link validation
            linkService.update(oldLink);
            return ResponseEntity.ok(new LinkModifyingResponse("ok"));
        } else {
            throw new ForbiddenException(OPERATION_FORBIDDEN_MSG);
        }
    }

    /**
     * Handles a request to refresh the expiration time of a link.
     *
     * @param id the UUID of the link to refresh
     * @return a ResponseEntity containing the response object indicating the success of the refresh operation
     * @throws ForbiddenException   if the authenticated user does not have rights to refresh the link
     * @throws DeletedLinkException if the link is already deleted
     */
    @PostMapping("/edit/refresh")
    public ResponseEntity<LinkModifyingResponse> refreshLink(@RequestParam UUID id) {
        if (doesUserHaveRightsForLinkById(id)) {
            Link oldLink = linkService.findById(id);
            if (oldLink.getStatus() == LinkStatus.DELETED) {
                throw new DeletedLinkException();
            }
            oldLink.setExpirationTime(LocalDateTime.now().plusDays(SHORT_LINK_LIFETIME_IN_DAYS));
            oldLink.setStatus(LinkStatus.ACTIVE);
            linkService.update(oldLink);
            return ResponseEntity.ok(new LinkModifyingResponse("ok"));
        } else {
            throw new ForbiddenException(OPERATION_FORBIDDEN_MSG);
        }
    }

    /**
     * Generates a new short link.
     *
     * @return a randomly generated short link of [A-Za-z0-9]
     */
    private String generateShortLink() {
        return RandomStringUtils.randomAlphanumeric(8);
    }

    /**
     * Checks if the authenticated user has rights to perform operations on a given link.
     *
     * @param linkId the UUID of the link to check
     * @return true if the user has rights, false otherwise
     */
    private boolean doesUserHaveRightsForLinkById(UUID linkId) {              //TODO: may be transformed into @?
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID linkUserId = linkService.findById(linkId).getUser().getId();
        UUID currentUserId = userService.findByEmail(authentication.getName()).getId();
        return linkUserId.equals(currentUserId);
    }
}
