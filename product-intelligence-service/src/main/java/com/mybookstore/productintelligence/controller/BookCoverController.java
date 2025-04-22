package com.mybookstore.productintelligence.controller;

import com.mybookstore.productintelligence.dto.BookCoverAnalysisResponse;
import com.mybookstore.productintelligence.dto.ImageCategoryResponse;
import com.mybookstore.productintelligence.service.GoogleVisionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/intelligence")
@ConditionalOnProperty(name = "ai.google-vision.enabled", havingValue = "true")
public class BookCoverController {

    private static final Logger log = LoggerFactory.getLogger(BookCoverController.class);
    private final GoogleVisionService visionService;

    public BookCoverController(GoogleVisionService visionService) {
        this.visionService = visionService;
    }

    /**
     * Endpoint for extracting text from book cover images
     * 
     * @param file   The book cover image file
     * @param bookId Optional book ID reference
     * @return Extracted text and metadata
     */
    @PostMapping(value = "/cover-text", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BookCoverAnalysisResponse> extractTextFromBookCover(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "bookId", required = false) String bookId) {

        log.info("Received request to extract text from book cover: {}", file.getOriginalFilename());

        BookCoverAnalysisResponse response = visionService.extractTextFromBookCover(file);

        if (bookId != null) {
            response.setBookId(bookId);
        }

        log.info("Successfully extracted text from book cover");
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for categorizing book cover images
     * 
     * @param file The book cover image file
     * @return Categories with confidence scores
     */
    @PostMapping(value = "/cover-categories", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageCategoryResponse> categorizeBookCover(
            @RequestParam("file") MultipartFile file) {

        log.info("Received request to categorize book cover: {}", file.getOriginalFilename());

        ImageCategoryResponse response = visionService.categorizeImage(file);

        log.info("Successfully categorized book cover with {} categories", response.getCategories().size());
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for extracting book metadata (title, author) from cover images
     * 
     * @param file   The book cover image file
     * @param bookId Optional book ID reference
     * @return Extracted metadata
     */
    @PostMapping(value = "/cover-metadata", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BookCoverAnalysisResponse> extractBookMetadata(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "bookId", required = false) String bookId) {

        log.info("Received request to extract metadata from book cover: {}", file.getOriginalFilename());

        BookCoverAnalysisResponse response = visionService.extractBookMetadata(file);

        if (bookId != null) {
            response.setBookId(bookId);
        }

        log.info("Successfully extracted metadata from book cover. Title: {}, Author: {}",
                response.getExtractedTitle(), response.getExtractedAuthor());
        return ResponseEntity.ok(response);
    }
}