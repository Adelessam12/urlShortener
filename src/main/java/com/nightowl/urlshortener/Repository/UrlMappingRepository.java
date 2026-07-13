package com.nightowl.urlshortener.Repository;

import com.nightowl.urlshortener.Models.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    Optional<UrlMapping> findByShortCode(String shortcode);
    Optional<UrlMapping> findByUrl(String url);
}
