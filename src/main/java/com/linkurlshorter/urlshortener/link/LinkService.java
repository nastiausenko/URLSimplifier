package com.linkurlshorter.urlshortener.link;

import com.linkurlshorter.urlshortener.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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
     * @throws NullLinkPropertyException If the 'link' parameter is null.
     */
    public Link save(Link link) {
        if (Objects.isNull(link)) {
            throw new NullLinkPropertyException();
        }
        return linkRepository.save(link);
    }

    /**
     * Updates a link entity.
     *
     * @param link The link entity to update.
     * @return The updated link entity.
     * @throws NullLinkPropertyException If the 'link' parameter is null.
     * @throws DeletedLinkException      If the link has been marked as deleted.
     */
    public Link update(Link link) {
        if (Objects.isNull(link)) {
            throw new NullLinkPropertyException();
        }

        throwIfDeleted(link);
        return linkRepository.save(link);
    }


    /**
     * Retrieves a link entity by its ID.
     *
     * @param id The ID of the link entity to retrieve.
     * @return The retrieved link entity.
     * @throws NullLinkPropertyException If the 'id' parameter is null.
     * @throws NoLinkFoundByIdException  If no link is found with the given ID.
     * @throws DeletedLinkException      If the retrieved link has been marked as deleted.
     */
    public Link findById(UUID id) {
        if (Objects.isNull(id)) {
            throw new NullLinkPropertyException();
        }
        Link link = linkRepository.findById(id).orElseThrow(NoLinkFoundByIdException::new);
        if (link.getStatus() == LinkStatus.DELETED) {
            throw new DeletedLinkException();
        }
        return link;
    }

    /**
     * Retrieves a link entity by its short link.
     *
     * @param shortLink The short link of the link entity to retrieve.
     * @return The retrieved link entity.
     * @throws NullLinkPropertyException       If the 'shortLink' parameter is null.
     * @throws NoLinkFoundByShortLinkException If no link is found with the given short link.
     * @throws DeletedLinkException            If the retrieved link has been marked as deleted.
     */
    public Link findByShortLink(String shortLink) {
        if (Objects.isNull(shortLink)) {
            throw new NullLinkPropertyException();
        }
        Link link = linkRepository.findByShortLink(shortLink).orElseThrow(NoLinkFoundByShortLinkException::new);
        if (link.getStatus() == LinkStatus.DELETED) {
            throw new DeletedLinkException();
        }
        return link;
    }

    public List<Link> findAllByUserId(UUID userId){
        if(Objects.isNull(userId)){
            throw new NullLinkPropertyException();
        }
        return linkRepository.findAllByUserId(userId);
    }

    public List<LinkStatisticsDto> getLinkUsageStatsByUserId(UUID userId){
        if(Objects.isNull(userId)){
            throw new NullLinkPropertyException();
        }
        return linkRepository.getLinkUsageStatsForUser(userId);
    }
    /**
     * Marks a link entity as deleted by its short link.
     *
     * @param shortLink The short link of the link entity to mark as deleted.
     * @throws NullLinkPropertyException       If the 'shortLink' parameter is null.
     * @throws NoLinkFoundByShortLinkException If no link is found with the given short link.
     * @throws DeletedLinkException            If the link has already been marked as deleted.
     */
    public void deleteByShortLink(String shortLink) {
        if (Objects.isNull(shortLink)) {
            throw new NullLinkPropertyException();
        }
        Link link = findByShortLink(shortLink);
        link.setStatus(LinkStatus.DELETED);
        linkRepository.save(link);
    }

    public void deleteById(UUID id) {        //TODO: needs test
        if (Objects.isNull(id)) {
            throw new NullLinkPropertyException();
        }
        linkRepository.deleteById(id);
    }
    /**
     * Searches for a unique existing link by a short link.
     * If an active link is found for the specified short link, returns that link.
     *
     * @param shortLink A string representing the short link to be searched.
     * @return The active link found for the specified short link.
     * @throws NoLinkFoundByShortLinkException If no link was found by the short link.
     * @throws NullLinkPropertyException       If the found link does not have the ACTIVE status.
     */
    public Link findByExistUniqueLink(String shortLink) {
        Link existingLink = linkRepository.findByShortLink(shortLink).orElseThrow(NoLinkFoundByShortLinkException::new);
        if (existingLink.getStatus() == LinkStatus.ACTIVE) {
            return existingLink;
        } else {
            throw new NullLinkPropertyException();
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
