package com.example.demo.external.newsapi;

public record ArticleDto(
        String author,
        String title,
        String description,
        String url,
        String urlToImage,
        String publishedAt
) {
}
