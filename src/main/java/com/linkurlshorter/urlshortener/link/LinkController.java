package com.linkurlshorter.urlshortener.link;

import com.linkurlshorter.urlshortener.user.User;
import com.linkurlshorter.urlshortener.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Controller for Link-related operations such as create, delete, update and get info + statistics
 *
 * @author Artem Poliakov
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/V1/link")
public class LinkController {
    private static final int SHORT_LINK_LIFETIME_IN_DAYS = 30;
    private static final String OPERATION_FORBIDDEN_MSG = "Operation forbidden!";

    private final LinkService linkService;
    private final UserService userService;
    private final LinkInfoDtoMapper linkDtoMapper;

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
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Create new link")
    public ResponseEntity<CreateLinkResponse> createLink(@RequestBody @Valid CreateLinkRequest createRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());
        String newShortUrl = generateShortLink();
        try {
            linkService.save(
                    Link.builder()
                            .longLink(createRequest.getLongLink())
                            .shortLink(newShortUrl)
                            .expirationTime(LocalDateTime.now().plusDays(SHORT_LINK_LIFETIME_IN_DAYS))
                            .user(user)
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
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Delete link by ID")
    public ResponseEntity<LinkModifyingResponse> deleteLink(@RequestParam UUID id) {
        if (doesUserHaveRightsForLinkById(id)) {
            linkService.deleteById(id);//TODO: add validations (id not null etc)
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
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Edit link content")
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
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Refresh link expiration time")
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
     * Retrieves information about a link using its short link.
     *
     * @param shortLink the short link of the link to retrieve information for
     * @return a ResponseEntity containing the response object with information about the link
     * @throws ForbiddenException if the authenticated user does not have rights to access the link
     */
    @GetMapping("/info")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get link info")
    public ResponseEntity<LinkInfoResponse> getInfoByShortLink(@RequestParam String shortLink) {
        Link link = linkService.findByShortLink(shortLink);
        if (doesUserHaveRightsForLinkById(link.getId())) {
            LinkInfoDto dto = linkDtoMapper.mapLinkToDto(link);
            LinkInfoResponse response = new LinkInfoResponse(List.of(dto), "ok");
            return ResponseEntity.ok(response);
        } else {
            throw new ForbiddenException(OPERATION_FORBIDDEN_MSG);
        }
    }

    /**
     * Retrieves information about all links associated with the authenticated user.
     *
     * @return a ResponseEntity containing the response object with information about all links for the user
     */
    @GetMapping("/all-links-info")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get all links info")
    public ResponseEntity<LinkInfoResponse> getAllLinksForUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID requesterUserId = userService.findByEmail(authentication.getName()).getId();
        List<LinkInfoDto> linksDto = linkService
                .findAllByUserId(requesterUserId)
                .stream()
                .map(linkDtoMapper::mapLinkToDto)
                .sorted(Comparator.comparing(LinkInfoDto::getUsageStatistics).reversed())
                .toList();
        return ResponseEntity.ok(new LinkInfoResponse(linksDto, "ok"));
    }

    /**
     * Retrieves usage statistics for all links associated with the authenticated user.
     *
     * @return a ResponseEntity containing the response object with usage statistics for all links for the user,
     * links are sorted in descending order
     */
    @GetMapping("/url-usage-top-for-user")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get all links usage statistics")
    public ResponseEntity<LinkStatisticsResponse> getLinksStatsForUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User requesterUser = userService.findByEmail(authentication.getName());
        List<LinkStatisticsDto> stats = linkService.getLinkUsageStatsByUserId(requesterUser.getId());
        stats.sort(Comparator.comparing(LinkStatisticsDto::getUsageStatistics).reversed());
        return ResponseEntity.ok(new LinkStatisticsResponse(stats, "ok"));
    }

    /**
     * Generates a new short link.
     *
     * @return a randomly generated short link of [A-Za-z0-9]
     */
    private String generateShortLink() {
        return RandomStringUtils.randomAlphanumeric(8);
    } //TODO: extracted into a servicethis method into service.

    /**
     * Checks if the authenticated user has rights to perform operations on a given link.
     *
     * @param linkId the UUID of the link to check
     * @return true if the user has rights, false otherwise
     */
    private boolean doesUserHaveRightsForLinkById(UUID linkId) {              //TODO: may be transformed into @? and extracted into a servicethis method into service.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID linkUserId = linkService.findById(linkId).getUser().getId();
        UUID currentUserId = userService.findByEmail(authentication.getName()).getId();
        return linkUserId.equals(currentUserId);
    }
}
