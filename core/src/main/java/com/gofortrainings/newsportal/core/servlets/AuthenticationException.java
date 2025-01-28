package com.gofortrainings.newsportal.core.servlets;

public class AuthenticationException extends Exception {

    // Constructor that takes a custom error message
    public AuthenticationException(String message) {
        super(message);
    }

    // Constructor that takes both a custom error message and a cause (another exception)
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor that takes only a cause (another exception)
    public AuthenticationException(Throwable cause) {
        super(cause);
    }
}
