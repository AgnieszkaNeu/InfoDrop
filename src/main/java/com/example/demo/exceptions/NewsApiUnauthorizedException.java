package com.example.demo.exceptions;

public class NewsApiUnauthorizedException extends RuntimeException{
    public NewsApiUnauthorizedException (){super("Your API key was missing from the request, or wasn't correct.");}
}
