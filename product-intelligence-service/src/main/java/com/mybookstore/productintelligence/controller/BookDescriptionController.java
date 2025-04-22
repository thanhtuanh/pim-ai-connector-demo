package com.mybookstore.productintelligence.controller;

import com.mybookstore.productintelligence.dto.ProductDescriptionRequest;
import com.mybookstore.productintelligence.dto.ProductDescriptionResponse;
import com.mybookstore.productintelligence.service.OpenAIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/intelligence")
@Tag(name = "Book Description Controller", description = "Erzeugt KI-basierte Buchbeschreibungen mit OpenAI")
public class BookDescriptionController {

    private static final Logger log = LoggerFactory.getLogger(BookDescriptionController.class);
    private final OpenAIService openAIService;

    public BookDescriptionController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @Operation(summary = "Erzeuge eine Buchbeschreibung", description = "Gib Titel, Autor, Genre usw. ein und erhalte eine automatisch generierte Buchbeschreibung.")
    @PostMapping("/book-description")
    public ResponseEntity<ProductDescriptionResponse> generateBookDescription(
            @Valid @RequestBody ProductDescriptionRequest request) {

        log.info("Received request to generate book description for: {}", request.getTitle());

        ProductDescriptionResponse response = openAIService.generateProductDescription(request);

        log.info("Successfully generated book description with {} tokens", response.getTotalTokens());
        return ResponseEntity.ok(response);
    }
}
