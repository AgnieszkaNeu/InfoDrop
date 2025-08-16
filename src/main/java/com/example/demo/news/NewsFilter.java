package com.example.demo.news;

import com.example.demo.User.AppUser;
import com.example.demo.User.AppUserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NewsFilter {

    final PersonalizedNewsService personalizedNewsService;

    final AppUserRepository appUserRepository;

    public NewsFilter(PersonalizedNewsService personalizedNewsService, AppUserRepository appUserRepository) {
        this.personalizedNewsService = personalizedNewsService;
        this.appUserRepository = appUserRepository;
    }

    public List<Article> fetchDailyNewsForUser(String email){
        AppUser user = appUserRepository.findAppUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Article> articles = personalizedNewsService.getPersonalizedNewsForUser(user);

        return articles.stream()
                .filter(article -> article.publishedAt() != null
                        && article.publishedAt().isAfter(LocalDate.now().minusDays(2))
                ).toList();
    }

}
