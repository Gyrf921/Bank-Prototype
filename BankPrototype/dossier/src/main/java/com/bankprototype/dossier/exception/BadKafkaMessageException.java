package com.bankprototype.dossier.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BadKafkaMessageException extends RuntimeException {
    public BadKafkaMessageException(String message) {
        super(message);
    }
}