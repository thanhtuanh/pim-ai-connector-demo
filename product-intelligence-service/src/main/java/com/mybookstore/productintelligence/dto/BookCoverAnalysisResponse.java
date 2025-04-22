package com.mybookstore.productintelligence.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookCoverAnalysisResponse {
    private String id;
    private String bookId;
    private String fileName;
    private long fileSize;
    private String contentType;
    private String fullText;
    private List<String> textElements;
    private String extractedTitle;
    private String extractedAuthor;

    @Builder.Default
    private LocalDateTime analysisTimestamp = LocalDateTime.now();
}