package com.bankprototype.deal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class SesCodeIsNotCorrectException extends RuntimeException {

    public SesCodeIsNotCorrectException(String message) {
        super(message);
    }


}