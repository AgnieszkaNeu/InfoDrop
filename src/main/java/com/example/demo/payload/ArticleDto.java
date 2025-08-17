package com.example.demo.payload;

public record ArticleDto(
        String author,
        String title,
        String description,
        String url,
        String urlToImage,
        String publishedAt
) {
}
