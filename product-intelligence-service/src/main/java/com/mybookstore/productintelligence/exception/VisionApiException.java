package com.mybookstore.productintelligence.exception;

public class VisionApiException extends RuntimeException {

    public VisionApiException(String message) {
        super(message);
    }

    public VisionApiException(String message, Throwable cause) {
        super(message, cause);
    }
}