package com.nightowl.urlshortener.Controller;

import com.nightowl.urlshortener.Dtos.UrlRequest;
import com.nightowl.urlshortener.Dtos.UrlResponse;
import com.nightowl.urlshortener.Models.UrlMapping;
import com.nightowl.urlshortener.Service.UrlShortenerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/urls")
@RequiredArgsConstructor
public class UrlShortenerController {
    private final UrlShortenerService urlShortenerService;

    @PostMapping
    public ResponseEntity<UrlResponse> CreateShortUrl(@Valid @RequestBody UrlRequest urlRequest) {
        UrlResponse urlResponse = urlShortenerService.createShortUrl(urlRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(urlResponse);

    }

    @GetMapping("/{ShortCode}")
    public ResponseEntity<UrlResponse> GetOriginalUrl(@PathVariable String ShortCode) {
        UrlResponse urlResponse = urlShortenerService.getOriginalUrl(ShortCode);
        if (urlResponse == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(urlResponse);
    }
    @GetMapping
    public ResponseEntity<List<UrlResponse>> getAllUrls() {
        return ResponseEntity.ok(urlShortenerService.getAllUrls());
    }
}
