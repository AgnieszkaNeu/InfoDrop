package com.example.demo.external.newsapi;

import java.util.List;

public record NewsApiResponse(
        String status,
        List<ArticleDto> articles
) { }
