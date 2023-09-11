package com.bankprototype.gateway.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ErrorDetails {

    private int statusCode;

    private int businessErrorCode;

    private LocalDate timestamp;

    private String message;

    private String details;


}
