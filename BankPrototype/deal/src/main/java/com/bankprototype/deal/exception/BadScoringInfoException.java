package com.bankprototype.deal.exception;


import com.bankprototype.deal.exception.global.ErrorDetails;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class BadScoringInfoException extends RuntimeException {

    private int responseCode;
    private ErrorDetails errorDetails;

    public BadScoringInfoException(int responseCode, ErrorDetails errorDetails, String message) {
        super(message);
        this.errorDetails = errorDetails;
        this.responseCode = responseCode;
    }
    public BadScoringInfoException(String message) {
        super(message);
    }


}
