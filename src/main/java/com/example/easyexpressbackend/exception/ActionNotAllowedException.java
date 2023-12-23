package com.example.easyexpressbackend.exception;

import com.example.easyexpressbackend.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class ActionNotAllowedException extends BaseException {
    public ActionNotAllowedException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}