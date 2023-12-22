package com.example.easyexpressbackend.exception.base;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = {BaseException.class})
    public ResponseEntity<Object> handleBaseException(BaseException e) {
        ApiResponse apiResponse = new ApiResponse(
                e.getMessage(),
                e.getHttpStatus(),
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(apiResponse, e.getHttpStatus());
    }
}
