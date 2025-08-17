package com.example.demo.payload;

import com.example.demo.news.CategoryType;
import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
    @NotBlank CategoryType category
){}
