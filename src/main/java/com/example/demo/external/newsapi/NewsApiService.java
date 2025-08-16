package com.example.demo.external.newsapi;

import com.example.demo.exceptions.NewsApiClientException;
import com.example.demo.exceptions.NewsApiServerException;
import com.example.demo.exceptions.NewsApiUnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class NewsApiService {

    private final String NEWS_API_HEADLINES = "https://newsapi.org/v2/top-headlines";

    @Value("${newsApi.token}")
    private String token;

    private final RestClient restClient;

    public NewsApiService(RestClient restClient) {
        this.restClient = restClient;
    }

    public NewsApiResponse returnNewsByCategory(String category) {
        URI uri = UriComponentsBuilder.fromUriString(NEWS_API_HEADLINES)
                .queryParam("country", "us")
                .queryParam("category", category)
                .build(true)
                .toUri();
        return call(uri);
    }

    public NewsApiResponse returnNewsByKeyword(String keyword){
        URI uri = UriComponentsBuilder.fromUriString(NEWS_API_HEADLINES)
                .queryParam("country", "us")
                .queryParam("q", keyword)
                .build(true)
                .toUri();
        return call(uri);
    }

    public NewsApiResponse call(URI uri){
        try {
            return restClient.get()
                    .uri(uri)
                    .header("x-api-key", token)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                        int statusCode = res.getStatusCode().value();
                        if (statusCode == 401) {
                            throw new NewsApiUnauthorizedException();
                        }
                        throw new NewsApiClientException(statusCode);
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                        throw new NewsApiServerException();
                    })
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (RestClientException ex){
            throw new RestClientException("NewsAPI I/O error: " + ex.getMessage());
        }
    }

}
