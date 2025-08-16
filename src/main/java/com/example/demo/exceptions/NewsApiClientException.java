package com.example.demo.exceptions;

public class NewsApiClientException extends RuntimeException{
    public NewsApiClientException(int statusCode) {super("NewsAPI client error: " + statusCode);}
}
