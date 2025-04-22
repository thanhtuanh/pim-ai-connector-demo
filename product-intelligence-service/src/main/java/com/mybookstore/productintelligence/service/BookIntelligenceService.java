package com.mybookstore.productintelligence.service;

import com.mybookstore.productintelligence.dto.AnalysisResponseDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "ai.metadata.enabled", havingValue = "true")
public class BookIntelligenceService {

        public AnalysisResponseDto getDummyAnalysis(Long bookId) {
                return AnalysisResponseDto.builder()
                                .bookId(bookId)
                                .detectedTitle("Dies ist eine automatisch generierte Analyse.")
                                .detectedAuthor("Max Mustermann")
                                .build();
        }
}
