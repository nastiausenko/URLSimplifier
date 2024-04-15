package com.linkurlshorter.urlshortener.link;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service class for managing link entities.
 */
@Service
@RequiredArgsConstructor
public class LinkService {
    private final LinkRepository linkRepository;

    /**
     * Saves a link entity.
     *
     * @param link The link entity to save.
     * @return The saved link entity.
     */
    public Link save(Link link) {
        return linkRepository.save(link);
    }

    /**
     * Updates a link entity.
     *
     * @param link The link entity to update.
     * @return The updated link entity.
     * @throws DeletedLinkException If the link has been marked as deleted.
     */
    public Link update(Link link) {
        throwIfDeleted(link);
        return linkRepository.save(link);
    }

    /**
     * Dynamically updates a link entity by its short link.
     *<strong>Does not update Id and UserId fields!!!</strong>
     * @param link       The link entity containing updated fields.
     * @param shortLink  The short link of the link entity to update.
     * @return The number of entities updated.
     * @throws DeletedLinkException      If the link has been marked as deleted.
     * @throws NoLinkFoundByShortLinkException  If no link is found with the given short link.
     */
    public int updateLinkByShortLinkDynamically(Link link, String shortLink) {
        throwIfDeleted(link);
        return linkRepository.updateLinkByShortLinkDynamically(link, shortLink);
    }

    /**
     * Retrieves a link entity by its ID.
     *
     * @param id The ID of the link entity to retrieve.
     * @return The retrieved link entity.
     * @throws NoLinkFoundByIdException If no link is found with the given ID.
     * @throws DeletedLinkException     If the retrieved link has been marked as deleted.
     */
    public Link findById(UUID id) {
        Link link = linkRepository.findById(id).orElseThrow(NoLinkFoundByIdException::new);
        throwIfDeleted(link);
        return link;
    }

    /**
     * Retrieves a link entity by its short link.
     *
     * @param shortLink The short link of the link entity to retrieve.
     * @return The retrieved link entity.
     * @throws NoLinkFoundByShortLinkException If no link is found with the given short link.
     * @throws DeletedLinkException           If the retrieved link has been marked as deleted.
     */
    public Link findByShortLink(String shortLink) {
        Link link = linkRepository.findByShortLink(shortLink).orElseThrow(NoLinkFoundByShortLinkException::new);
        throwIfDeleted(link);
        return link;
    }

    /**
     * Marks a link entity as deleted by its short link.
     *
     * @param shortLink The short link of the link entity to mark as deleted.
     * @throws NoLinkFoundByShortLinkException If no link is found with the given short link.
     * @throws DeletedLinkException           If the link has already been marked as deleted.
     */
    public void deleteByShortLink(String shortLink) {
        Link link = Link.builder()
                .status(LinkStatus.DELETED)
                .build();
        int alteredCount = updateLinkByShortLinkDynamically(link, shortLink);
        if (alteredCount <= 0) {
            throw new NoLinkFoundByShortLinkException();
        }
    }

    /**
     * Throws a DeletedLinkException if the link has been marked as deleted.
     *
     * @param link The link entity to check.
     * @throws DeletedLinkException If the link has been marked as deleted.
     */
    private void throwIfDeleted(Link link) {
        if (findByShortLink(link.getShortLink()).getStatus() == LinkStatus.DELETED) {
            throw new DeletedLinkException();
        }
    }
}
