package com.mybookstore.productintelligence.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookId;
    private String detectedTitle;
    private String detectedAuthor;

    @ElementCollection
    private List<String> suggestedCategories;

    private Double confidenceScore;

    @Column(length = 2000)
    private String generatedDescription;

    private String coverImageUrl;
}