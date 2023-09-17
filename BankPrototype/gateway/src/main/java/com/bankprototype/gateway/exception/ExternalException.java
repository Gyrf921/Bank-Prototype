package com.bankprototype.gateway.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class ExternalException extends RuntimeException {

    private int responseCode;
    private ErrorDetails errorDetails;

    public ExternalException(int responseCode, ErrorDetails errorDetails, String message) {
        super(message);
        this.errorDetails = errorDetails;
        this.responseCode = responseCode;
    }
}
