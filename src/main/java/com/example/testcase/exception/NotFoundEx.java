package com.example.testcase.exception;

public class NotFoundEx extends RuntimeException {
    public NotFoundEx(String message){
        super(message);
    }
}
