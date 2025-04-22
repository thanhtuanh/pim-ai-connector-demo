package com.mybookstore.productintelligence.exception;

public class OpenAIApiException extends RuntimeException {

    public OpenAIApiException(String message) {
        super(message);
    }

    public OpenAIApiException(String message, Throwable cause) {
        super(message, cause);
    }
}