package com.example.demo.external.newsapi;

import com.example.demo.payload.ArticleDto;

import java.util.List;

public record NewsApiResponse(
        String status,
        List<ArticleDto> articles
) { }
