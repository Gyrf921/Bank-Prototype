package com.bankprototype.dossier.exception;

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

    private static final int BUSINESS_ERROR_CODE_EMAIL = 300;
    private static final int BUSINESS_ERROR_CODE_KAFKA_DATA = 330;
    private static final int BUSINESS_ERROR_CODE_SERVER = 0;

    @ExceptionHandler(CreateMimeMessageException.class)
    public ResponseEntity<?> createMimeMessageException(CreateMimeMessageException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST.value(), BUSINESS_ERROR_CODE_EMAIL, LocalDate.now(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadKafkaMessageException.class)
    public ResponseEntity<?> badKafkaMessageException(BadKafkaMessageException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.UNPROCESSABLE_ENTITY.value(), BUSINESS_ERROR_CODE_KAFKA_DATA, LocalDate.now(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.UNPROCESSABLE_ENTITY);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR.value(), BUSINESS_ERROR_CODE_SERVER, LocalDate.now(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
