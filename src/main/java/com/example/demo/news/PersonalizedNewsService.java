package com.example.demo.news;

import com.example.demo.User.AppUser;
import com.example.demo.external.newsapi.ArticleDto;
import com.example.demo.external.newsapi.NewsApiResponse;
import com.example.demo.external.newsapi.NewsApiService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PersonalizedNewsService {

    final NewsApiService newsApiService;

    final ArticleMapper articleMapper;

    public PersonalizedNewsService(NewsApiService newsApiService,ArticleMapper articleMapper) {
        this.newsApiService = newsApiService;
        this.articleMapper = articleMapper;
    }

    public List<Article> getPersonalizedNewsForUser(AppUser user){
        Set<String> categories = user.getCategories();
        Set<String> keywords = user.getKeywords();

        return fetchArticlesByCategoriesAndKeywords(categories, keywords);
    }

    public List<Article> fetchArticlesByCategoriesAndKeywords(Set<String> categories, Set<String> keywords){

        if(categories.isEmpty() && keywords.isEmpty()){
            return List.of();
        }

        Stream<ArticleDto> articleDtoStreamByCategory = categories.stream()
                .map(newsApiService::returnNewsByCategory)
                .map(NewsApiResponse::articles)
                .flatMap(Collection::stream);

        Stream<ArticleDto> articleDtoStreamByKeyword = keywords.stream()
                .map(newsApiService::returnNewsByKeyword)
                .map(NewsApiResponse::articles)
                .flatMap(Collection::stream);

        return  Stream.concat(articleDtoStreamByCategory,articleDtoStreamByKeyword)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toMap(
                                ArticleDto::url,
                                Function.identity(),
                                (existing, replacement) -> existing
                        ))
                        .values()
                        .stream()
                        .map(articleMapper::articleDtoToArticle)
                        .toList();
    }

}
