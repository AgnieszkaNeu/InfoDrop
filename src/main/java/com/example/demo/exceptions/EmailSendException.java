package com.example.demo.exceptions;

public class EmailSendException extends RuntimeException{
    public EmailSendException(String message) {super(message);}
}
