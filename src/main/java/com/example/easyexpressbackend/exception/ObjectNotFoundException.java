package com.example.easyexpressbackend.exception;

import com.example.easyexpressbackend.exception.base.BaseException;
import org.springframework.http.HttpStatus;

public class ObjectNotFoundException extends BaseException {

    public ObjectNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
