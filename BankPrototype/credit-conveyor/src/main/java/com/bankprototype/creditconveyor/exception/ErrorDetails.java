package com.bankprototype.creditconveyor.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@Data
@AllArgsConstructor
public class ErrorDetails {

    private int statusCode;

    private int business_error_code;

    private LocalDate timestamp;

    private String message;

    private String details;


}
