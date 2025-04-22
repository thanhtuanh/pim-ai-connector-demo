package com.mybookstore.productintelligence.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repräsentiert die Antwort der Bildkategorisierung durch die Google Vision
 * API.
 * Enthält Informationen über erkannte Kategorien und ihre Konfidenzwerte.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageCategoryResponse {
    /**
     * Eindeutige ID der Antwort
     */
    private String id;

    /**
     * Name der analysierten Bilddatei
     */
    private String fileName;

    /**
     * Liste von Kategorien mit ihren Konfidenzwerten
     */
    private List<CategoryResult> categories;

    /**
     * Zeitpunkt der Analyse
     */
    @Builder.Default
    private LocalDateTime analysisTimestamp = LocalDateTime.now();
}