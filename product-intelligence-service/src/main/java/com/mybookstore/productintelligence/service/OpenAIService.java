package com.mybookstore.productintelligence.service;

import com.mybookstore.productintelligence.dto.ProductDescriptionRequest;
import com.mybookstore.productintelligence.dto.ProductDescriptionResponse;
import com.mybookstore.productintelligence.exception.OpenAIApiException;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class OpenAIService {

    private static final Logger log = LoggerFactory.getLogger(OpenAIService.class);
    private final OpenAiService openAiClient;
    private final String model;

    // Direkte Verwendung der in application.properties definierten Werte
    public OpenAIService(@Value("${ai.openai.api-key}") String apiKey,
            @Value("${ai.openai.model:gpt-3.5-turbo}") String model) {
        this.openAiClient = new OpenAiService(apiKey, Duration.ofSeconds(60));
        this.model = model;
    }

    /**
     * Generates a product description based on provided book details
     * 
     * @param request The request containing book details
     * @return ProductDescriptionResponse with generated content
     * @throws OpenAIApiException if there's an error with the OpenAI API call
     */
    public ProductDescriptionResponse generateProductDescription(ProductDescriptionRequest request) {
        try {
            // Create the message for OpenAI
            List<ChatMessage> messages = new ArrayList<>();
            ChatMessage systemMessage = new ChatMessage("system",
                    "You are an experienced book marketing expert who writes engaging product descriptions.");
            messages.add(systemMessage);

            ChatMessage userMessage = new ChatMessage("user", buildPrompt(request));
            messages.add(userMessage);

            // Create the request
            ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                    .model(model)
                    .messages(messages)
                    .temperature(0.7)
                    .maxTokens(500)
                    .build();

            // Send the request to OpenAI
            ChatCompletionResult result = openAiClient.createChatCompletion(completionRequest);

            // Create the response
            return ProductDescriptionResponse.builder()
                    .description(result.getChoices().get(0).getMessage().getContent())
                    .model(result.getModel())
                    .promptTokens(result.getUsage().getPromptTokens())
                    .completionTokens(result.getUsage().getCompletionTokens())
                    .totalTokens(result.getUsage().getTotalTokens())
                    .build();
        } catch (Exception e) {
            log.error("Error generating product description: {}", e.getMessage());
            throw new OpenAIApiException("Error generating product description", e);
        }
    }

    /**
     * Builds the prompt for OpenAI based on book details
     */
    private String buildPrompt(ProductDescriptionRequest request) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Create a compelling product description for a book with the following details:\n\n");

        if (request.getTitle() != null) {
            prompt.append("Title: ").append(request.getTitle()).append("\n");
        }

        if (request.getAuthor() != null) {
            prompt.append("Author: ").append(request.getAuthor()).append("\n");
        }

        if (request.getGenre() != null) {
            prompt.append("Genre: ").append(request.getGenre()).append("\n");
        }

        if (request.getKeywords() != null && !request.getKeywords().isEmpty()) {
            prompt.append("Keywords: ").append(String.join(", ", request.getKeywords())).append("\n");
        }

        if (request.getPageCount() > 0) {
            prompt.append("Page Count: ").append(request.getPageCount()).append("\n");
        }

        if (request.getTargetAudience() != null) {
            prompt.append("Target Audience: ").append(request.getTargetAudience()).append("\n");
        }

        if (request.getAdditionalInfo() != null) {
            prompt.append("\nAdditional Information: ").append(request.getAdditionalInfo()).append("\n");
        }

        prompt.append(
                "\nPlease create a marketing description of about 200-300 words that highlights the book's unique features, engages the target audience, and encourages readers to purchase it. The description should be professional, compelling, and suitable for an online bookstore.");

        return prompt.toString();
    }
}