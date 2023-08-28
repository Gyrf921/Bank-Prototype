package com.bankprototype.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BirthdateException extends RuntimeException {
    public BirthdateException(String message) {
        super(message);
    }
}
