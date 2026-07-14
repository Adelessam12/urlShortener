package com.nightowl.urlshortener.Service;

import com.nightowl.urlshortener.Dtos.UrlRequest;
import com.nightowl.urlshortener.Dtos.UrlResponse;
import com.nightowl.urlshortener.Models.UrlMapping;
import com.nightowl.urlshortener.Repository.UrlMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final UrlMappingRepository repository;
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int SHORT_CODE_LENGTH = 6;
    private final Random random = new Random();

    public UrlResponse createShortUrl(UrlRequest request) {
        // Check if URL already exists in the database
        Optional<UrlMapping> existingMapping = repository.findByUrl(request.getUrl());
        if (existingMapping.isPresent()) {
            UrlResponse response = mapToResponse(existingMapping.get());
            response.setMessage("already in db");
            return response;
        }

        String shortCode = generateUniqueShortCode();

        UrlMapping mapping = UrlMapping.builder()
                .shortCode(shortCode)
                .url(request.getUrl())
                .build();

        mapping = repository.save(mapping);
        UrlResponse response = mapToResponse(mapping);
        response.setMessage("successfully created");
        return response;
    }

    @Cacheable(value = "urls", key = "#shortCode")
    public UrlResponse getOriginalUrl(String shortCode) {
        return repository.findByShortCode(shortCode)
                .map(this::mapToResponse)
                .orElse(null); // The controller will handle null to return 404
    }

    public List<UrlResponse> getAllUrls() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private String generateUniqueShortCode() {
        String shortCode;
        do {
            StringBuilder sb = new StringBuilder(SHORT_CODE_LENGTH);
            for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
                sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
            }
            shortCode = sb.toString();
        } while (repository.findByShortCode(shortCode).isPresent());
        return shortCode;
    }

    private UrlResponse mapToResponse(UrlMapping mapping) {
        return UrlResponse.builder()
                .id(mapping.getId())
                .shortCode(mapping.getShortCode())
                .url(mapping.getUrl())
                .build();
    }
}
