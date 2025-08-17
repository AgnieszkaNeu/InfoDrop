package com.example.demo.payload;

import jakarta.validation.constraints.NotBlank;

public record KeywordRequest(
    @NotBlank String keyword
) {}
