package com.nightowl.urlshortener.Dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class UrlRequest {
    @NotBlank(message = "Url cannot be empty")
    @URL(message = "Invalid Url format")
    private String url;
}
