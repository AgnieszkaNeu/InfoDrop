package com.example.demo.news;

import java.time.LocalDate;

public record Article (
        String author,
        String title,
        String description,
        String url,
        String urlToImage,
        LocalDate publishedAt
) {
}
