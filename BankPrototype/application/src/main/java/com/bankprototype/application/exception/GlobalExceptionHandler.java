package com.bankprototype.application.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final int BUSINESS_ERROR_CODE_VALIDATE = 100;
    private static final int BUSINESS_ERROR_CODE_SERVER = 0;

    @ExceptionHandler(BirthdateException.class)
    public ResponseEntity<?> badBirthdateException(BirthdateException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST.value(), BUSINESS_ERROR_CODE_VALIDATE, LocalDate.now(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExternalException.class)
    public ResponseEntity<?> externalExceptionValidation(ExternalException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST.value(), BUSINESS_ERROR_CODE_VALIDATE, LocalDate.now(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.value(), BUSINESS_ERROR_CODE_SERVER, LocalDate.now(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
