package com.mybookstore.productintelligence.dto;

import lombok.Data;

@Data
public class CategoryResult {
    private String name;
    private float confidence;
}