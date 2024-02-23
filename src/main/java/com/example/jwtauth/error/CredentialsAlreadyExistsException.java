package com.example.jwtauth.error;

public class CredentialsAlreadyExistsException extends RuntimeException {
    public CredentialsAlreadyExistsException(String message) {
        super(message);
    }
}