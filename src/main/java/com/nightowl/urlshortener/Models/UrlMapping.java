package com.nightowl.urlshortener.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="url_mappings")
@Data //generate get set equals hashCode tostring on compilation
@NoArgsConstructor //auto generate empty argument constructor
@AllArgsConstructor// auto generate constructor with all attributes as args
@Builder // uses builder pattern for constructor , UrlMappings.builder
public class UrlMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,unique = true,length = 10)
    private String shortCode;

    @Column(nullable = false,length = 2048)
    private String url;
}
