package com.mybookstore.productintelligence.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Antwortobjekt mit der von OpenAI generierten Buchbeschreibung.")
public class ProductDescriptionResponse {

    @Schema(description = "Die von OpenAI generierte Buchbeschreibung")
    private String description;

    @Schema(description = "Name des verwendeten OpenAI-Modells")
    private String model;

    @Schema(description = "Anzahl der Tokens im Prompt")
    private long promptTokens;

    @Schema(description = "Anzahl der Tokens in der Antwort (Completion)")
    private long completionTokens;

    @Schema(description = "Gesamte Token-Anzahl des Requests")
    private long totalTokens;
}
