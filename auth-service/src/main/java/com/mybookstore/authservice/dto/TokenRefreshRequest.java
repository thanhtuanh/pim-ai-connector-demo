package com.mybookstore.authservice.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class TokenRefreshRequest {
    @NotBlank
    private String refreshToken;
}