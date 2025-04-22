package com.mybookstore.productintelligence.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "Anfrageobjekt zur Erzeugung einer KI-basierten Buchbeschreibung.", example = """
            {
              "title": "Clean Code",
              "author": "Robert C. Martin",
              "genre": "Software Engineering",
              "keywords": ["Clean Code", "Best Practices"],
              "pageCount": 464,
              "targetAudience": "Softwareentwickler",
              "additionalInfo": "Ein Muss für jeden Entwickler"
            }
        """)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDescriptionRequest {

    @NotBlank
    @Schema(description = "Titel des Buches", example = "Clean Code")
    private String title;

    @NotBlank
    @Schema(description = "Autor des Buches", example = "Robert C. Martin")
    private String author;

    @NotBlank
    @Schema(description = "Genre oder Kategorie", example = "Software Engineering")
    private String genre;

    @NotEmpty
    @Schema(description = "Relevante Schlagworte", example = "[\"Clean Code\", \"Best Practices\"]")
    private List<String> keywords;

    @Min(1)
    @Schema(description = "Anzahl der Seiten", example = "464")
    private int pageCount;

    @NotBlank
    @Schema(description = "Zielgruppe des Buches", example = "Softwareentwickler")
    private String targetAudience;

    @Schema(description = "Zusätzliche Informationen", example = "Ein Muss für jeden Entwickler")
    private String additionalInfo;
}
