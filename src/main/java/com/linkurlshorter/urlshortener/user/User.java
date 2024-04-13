package com.linkurlshorter.urlshortener.user;

import com.linkurlshorter.urlshortener.link.Link;
import com.linkurlshorter.urlshortener.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Represents a user in the system.
 *
 * <p>Encapsulates user information including their unique identifier, email address, password,
 * role, and associated links.
 *
 * <p><strong>Author:</strong> Artem Poliakov
 *
 * @see Link
 * @see UserRole
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Link> links;
}
