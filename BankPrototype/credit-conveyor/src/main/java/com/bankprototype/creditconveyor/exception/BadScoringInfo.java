package com.bankprototype.creditconveyor.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadScoringInfo extends RuntimeException{

    public BadScoringInfo(String message){
        super(message);
    }


}
