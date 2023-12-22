package com.example.easyexpressbackend.exception;

import com.example.easyexpressbackend.exception.base.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionNotAllowedException extends BaseException {
    public ActionNotAllowedException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}