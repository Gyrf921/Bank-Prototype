package com.bankprototype.dossier.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CreateMimeMessageException extends RuntimeException {

    public CreateMimeMessageException(String message) {
        super(message);
    }
}
