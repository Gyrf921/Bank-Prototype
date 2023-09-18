package com.bankprototype.deal.exception;

import com.bankprototype.deal.exception.global.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ExternalException extends RuntimeException {
    private int responseCode;
    private ErrorDetails errorDetails;

    public ExternalException(int responseCode, ErrorDetails errorDetails, String message) {
        super(message);
        this.errorDetails = errorDetails;
        this.responseCode = responseCode;
    }
}
