package com.linkurlshorter.urlshortener.link;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface LinkRepository extends JpaRepository<Link, UUID> {
}
