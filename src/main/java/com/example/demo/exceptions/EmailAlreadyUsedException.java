package com.example.demo.exceptions;

public class EmailAlreadyUsedException extends RuntimeException{

    public EmailAlreadyUsedException(String email) {super("A user with this email already exists: " + email);}
}
