package com.linkurlshorter.urlshortener.link.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.linkurlshorter.urlshortener.user.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a link in the system.
 *
 * <p>Encapsulates information about a short link, including its unique identifier, long URL,
 * short URL, associated user, creation time, expiration time, usage statistics, and status. Created on: 13.04.2024
 *
 * @author Artem Poliakov
 * @version 1.0
 * @see User
 * @see LinkStatus
 */
@Entity
@DynamicUpdate
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
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdTime = LocalDateTime.now();
    @Column(name = "expiration_time")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime expirationTime;
    @Column(name = "statistics")
    private int statistics;
    @Column(name = "status")
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private LinkStatus status = LinkStatus.ACTIVE;
}
