package com.linkurlshorter.urlshortener.link;

import com.linkurlshorter.urlshortener.link.enums.LinkStatus;
import com.linkurlshorter.urlshortener.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a link in the system.
 *
 * <p>Encapsulates information about a short link, including its unique identifier, long URL,
 * short URL, associated user, creation time, expiration time, usage statistics, and status.
 *
 * <p><strong>Author:</strong> Artem Poliakov
 *
 * @see User
 * @see LinkStatus
 */
@Entity
@Table(name = "links")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;
    @Column(name = "long_link")
    private String longLink;
    @Column(name = "short_link")
    private String shortLink;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "created_time")
    @Builder.Default
    private LocalDateTime createdTime = LocalDateTime.now();
    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;
    @Column(name = "statistics")
    private int statistics;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private LinkStatus status;
}
