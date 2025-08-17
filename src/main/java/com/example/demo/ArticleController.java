package com.example.demo;

import com.example.demo.mailConfig.CustomMailSender;
import com.example.demo.news.Article;
import com.example.demo.news.NewsFilter;
import com.example.demo.payload.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ArticleController {

    NewsFilter newsFilter;

   CustomMailSender customMailSender;

    public ArticleController(NewsFilter newsFilter, CustomMailSender customMailSender) {
        this.newsFilter = newsFilter;
        this.customMailSender = customMailSender;
    }

    @GetMapping("/articles")
    public ResponseEntity<List<Article>> getArticles(Authentication authentication){
        List<Article> articles = newsFilter.fetchDailyNewsForUser(authentication.getName());
        return ResponseEntity.ok().body(articles);
    }

    @GetMapping("/send")
    public ResponseEntity<ApiResponse> send(Authentication authentication){
        customMailSender.sendEmail(authentication.getName());
        return ResponseEntity.ok().body(new ApiResponse("Newsletter sent to" + authentication.getName()));
    }
}
