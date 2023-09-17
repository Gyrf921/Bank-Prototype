package com.bankprototype.deal.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class BadScoringInfoException extends RuntimeException {

    public BadScoringInfoException(String message) {
        super(message);
    }


}
