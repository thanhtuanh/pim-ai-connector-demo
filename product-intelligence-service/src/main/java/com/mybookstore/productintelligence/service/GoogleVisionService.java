package com.mybookstore.productintelligence.service;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import com.mybookstore.productintelligence.dto.BookCoverAnalysisResponse;
import com.mybookstore.productintelligence.dto.CategoryResult;
import com.mybookstore.productintelligence.dto.ImageCategoryResponse;
import com.mybookstore.productintelligence.exception.VisionApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//@Service
public class GoogleVisionService {

    private static final Logger log = LoggerFactory.getLogger(GoogleVisionService.class);

    @Value("${ai.google-vision.credentials-path}")
    private String credentialsPath;

    /**
     * Creates and returns an ImageAnnotatorClient
     */
    private ImageAnnotatorClient createImageAnnotatorClient() throws IOException {
        // Standard-Zugriff über die Umgebungsvariable GOOGLE_APPLICATION_CREDENTIALS
        return ImageAnnotatorClient.create();
    }

    /**
     * Extracts text from a book cover image
     * 
     * @param file Image file of the book cover
     * @return BookCoverAnalysisResponse with extracted text and metadata
     * @throws VisionApiException if there's an error with the Google Vision API
     */
    public BookCoverAnalysisResponse extractTextFromBookCover(MultipartFile file) {
        try {
            // Create the image to analyze
            ByteString imgBytes = ByteString.copyFrom(file.getBytes());
            Image img = Image.newBuilder().setContent(imgBytes).build();

            // Create the text detection request
            Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feat)
                    .setImage(img)
                    .build();

            // Create the client and send the request
            try (ImageAnnotatorClient client = createImageAnnotatorClient()) {
                BatchAnnotateImagesResponse response = client.batchAnnotateImages(List.of(request));
                List<AnnotateImageResponse> responses = response.getResponsesList();

                // Process the response
                for (AnnotateImageResponse res : responses) {
                    if (res.hasError()) {
                        log.error("Error from Google Vision API: {}", res.getError().getMessage());
                        throw new VisionApiException("Error from Google Vision API: " + res.getError().getMessage());
                    }

                    // Extract text annotations
                    TextAnnotation fullText = res.getFullTextAnnotation();
                    List<EntityAnnotation> textAnnotations = res.getTextAnnotationsList();

                    BookCoverAnalysisResponse.BookCoverAnalysisResponseBuilder result = BookCoverAnalysisResponse
                            .builder()
                            .fileName(file.getOriginalFilename())
                            .fileSize(file.getSize())
                            .contentType(file.getContentType());

                    // Set full text if available
                    if (fullText != null) {
                        result.fullText(fullText.getText());
                    } else if (!textAnnotations.isEmpty()) {
                        // The first annotation contains the entire extracted text
                        result.fullText(textAnnotations.get(0).getDescription());

                        // The rest are individual words/elements
                        List<String> textElements = textAnnotations.stream()
                                .skip(1) // Skip the first one as it contains the full text
                                .map(EntityAnnotation::getDescription)
                                .collect(Collectors.toList());
                        result.textElements(textElements);
                    }

                    return result.build();
                }
            }

            throw new VisionApiException("No response received from Google Vision API");
        } catch (IOException e) {
            log.error("Error processing file or communicating with Google Vision API: {}", e.getMessage());
            throw new VisionApiException("Error processing image file", e);
        } catch (Exception e) {
            log.error("Unexpected error during book cover analysis: {}", e.getMessage());
            throw new VisionApiException("Failed to analyze book cover", e);
        }
    }

    /**
     * Categorizes an image using Google Vision API
     * 
     * @param file The image file to categorize
     * @return ImageCategoryResponse with categories and confidence scores
     * @throws VisionApiException if there's an error with the Google Vision API
     */
    public ImageCategoryResponse categorizeImage(MultipartFile file) {
        try {
            // Create the image to analyze
            ByteString imgBytes = ByteString.copyFrom(file.getBytes());
            Image img = Image.newBuilder().setContent(imgBytes).build();

            // Create the label detection request
            Feature feat = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION)
                    .setMaxResults(10)
                    .build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feat)
                    .setImage(img)
                    .build();

            // Create the client and send the request
            try (ImageAnnotatorClient client = createImageAnnotatorClient()) {
                BatchAnnotateImagesResponse response = client.batchAnnotateImages(List.of(request));
                List<AnnotateImageResponse> responses = response.getResponsesList();

                // Process the response
                for (AnnotateImageResponse res : responses) {
                    if (res.hasError()) {
                        log.error("Error from Google Vision API: {}", res.getError().getMessage());
                        throw new VisionApiException("Error from Google Vision API: " + res.getError().getMessage());
                    }

                    // Extract label annotations
                    List<EntityAnnotation> labels = res.getLabelAnnotationsList();

                    ImageCategoryResponse.ImageCategoryResponseBuilder result = ImageCategoryResponse.builder()
                            .fileName(file.getOriginalFilename());

                    // Create a list of categories with confidence scores
                    List<CategoryResult> categories = new ArrayList<>();
                    for (EntityAnnotation label : labels) {
                        CategoryResult category = new CategoryResult();
                        category.setName(label.getDescription());
                        category.setConfidence(label.getScore());
                        categories.add(category);
                    }

                    result.categories(categories);
                    return result.build();
                }
            }

            throw new VisionApiException("No response received from Google Vision API");
        } catch (IOException e) {
            log.error("Error processing file or communicating with Google Vision API: {}", e.getMessage());
            throw new VisionApiException("Error processing image file", e);
        } catch (Exception e) {
            log.error("Unexpected error during image categorization: {}", e.getMessage());
            throw new VisionApiException("Failed to categorize image", e);
        }
    }

    /**
     * Attempts to extract book metadata from a cover image
     * 
     * @param file The book cover image
     * @return Extracted metadata like title, author, etc.
     */
    public BookCoverAnalysisResponse extractBookMetadata(MultipartFile file) {
        BookCoverAnalysisResponse textResponse = extractTextFromBookCover(file);

        // Simple heuristic to identify potential title and author
        // This is a basic implementation and can be enhanced with more sophisticated
        // text analysis or machine learning approaches
        if (textResponse.getTextElements() != null && !textResponse.getTextElements().isEmpty()) {
            List<String> elements = textResponse.getTextElements();

            BookCoverAnalysisResponse.BookCoverAnalysisResponseBuilder builder = BookCoverAnalysisResponse.builder()
                    .id(textResponse.getId())
                    .bookId(textResponse.getBookId())
                    .fileName(textResponse.getFileName())
                    .fileSize(textResponse.getFileSize())
                    .contentType(textResponse.getContentType())
                    .fullText(textResponse.getFullText())
                    .textElements(textResponse.getTextElements());

            // Assume the largest text on cover might be the title
            if (elements.size() > 0) {
                builder.extractedTitle(elements.get(0));
            }

            // Look for text preceded by "by" which might indicate the author
            for (int i = 0; i < elements.size() - 1; i++) {
                if (elements.get(i).equalsIgnoreCase("by")) {
                    builder.extractedAuthor(elements.get(i + 1));
                    break;
                }
            }

            // If no "by" found, try a different heuristic
            if (textResponse.getExtractedAuthor() == null && elements.size() > 1) {
                // Assume the author might be the second most prominent text
                builder.extractedAuthor(elements.get(1));
            }

            return builder.build();
        }

        return textResponse;
    }
}