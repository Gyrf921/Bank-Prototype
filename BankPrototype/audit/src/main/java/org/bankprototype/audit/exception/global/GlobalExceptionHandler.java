package org.bankprototype.audit.exception.global;

import lombok.extern.slf4j.Slf4j;
import org.bankprototype.audit.exception.BadKafkaMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BadKafkaMessageException.class)
    public ResponseEntity<?> badScoringInfoException(BadKafkaMessageException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        ErrorDetails errorDetails = new ErrorDetails(HttpStatus.BAD_REQUEST.value(), ConstantErrorCode.BUSINESS_ERROR_CODE_SERVER, LocalDate.now(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
