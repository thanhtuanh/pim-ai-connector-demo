package com.mybookstore.productintelligence.controller;

import com.mybookstore.productintelligence.dto.AnalysisResponseDto;
import com.mybookstore.productintelligence.service.BookIntelligenceService;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ConditionalOnProperty(name = "ai.metadata.enabled", havingValue = "true")
@RequestMapping("/api/intelligence")
@RequiredArgsConstructor
public class BookIntelligenceController {
    private final BookIntelligenceService bookIntelligenceService;

    @GetMapping("/analysis/{bookId}")
    public ResponseEntity<AnalysisResponseDto> getAnalysisForBook(@PathVariable Long bookId) {
        AnalysisResponseDto response = bookIntelligenceService.getDummyAnalysis(bookId);
        return ResponseEntity.ok(response);
    }
}