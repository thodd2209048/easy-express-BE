package com.example.easyexpressbackend.exception;

import com.example.easyexpressbackend.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class DuplicateObjectException extends BaseException {
    public DuplicateObjectException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}