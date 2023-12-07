package com.example.testcase.exception;

public class AuthenticationEx extends RuntimeException {

    public AuthenticationEx(String message) {
        super(message);
    }
}
