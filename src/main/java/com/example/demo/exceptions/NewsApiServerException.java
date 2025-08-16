package com.example.demo.exceptions;

public class NewsApiServerException extends RuntimeException{
    public NewsApiServerException() {super("NewsAPI server error");}
}
