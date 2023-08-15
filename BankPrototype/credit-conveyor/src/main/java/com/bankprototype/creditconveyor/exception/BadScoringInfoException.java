package com.bankprototype.creditconveyor.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadScoringInfoException extends RuntimeException{


    public BadScoringInfoException(String message){
        super(message);
    }


}
