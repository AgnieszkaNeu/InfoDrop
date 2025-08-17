package com.example.demo.news;

import com.example.demo.payload.ArticleDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;

@Component
public class ArticleMapper {

    public static final Logger log = LoggerFactory.getLogger(ArticleMapper.class);

    public Article articleDtoToArticle(ArticleDto articleDto){
        LocalDate publishedAt = parseDate(articleDto.publishedAt());

        return new Article(
                articleDto.author(),
                articleDto.title(),
                articleDto.description(),
                articleDto.url(),
                articleDto.urlToImage(),
                publishedAt
        );
    }

    private LocalDate parseDate (String date) {
        try {
            if (date.endsWith("Z")){
                return OffsetDateTime.parse(date).toLocalDate();
            }
            if (date.matches(".*[+-]\\d{2}:\\d{2}$")) {
                return OffsetDateTime.parse(date)
                                .atZoneSameInstant(ZoneId.systemDefault())
                                .toLocalDate();
            }
            else {return LocalDate.parse(date);}

        } catch (DateTimeParseException e){
            log.info("Invalid date type: " + date);
        }
        return null;
    }
}
