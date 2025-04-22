package com.mybookstore.productintelligence.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class AnalysisResponseDto {
    private Long bookId;
    private String detectedTitle;
    private String detectedAuthor;
    private List<String> suggestedCategories;
    private String generatedDescription;
    private Double confidenceScore;
}