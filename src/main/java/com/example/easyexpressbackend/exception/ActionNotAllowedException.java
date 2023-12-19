package com.example.easyexpressbackend.exception;

public class ActionNotAllowedException extends RuntimeException{
    public ActionNotAllowedException(String message) {
        super(message);
    }

    public ActionNotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }
}