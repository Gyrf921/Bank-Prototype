package com.bankprototype.deal.exception.global;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {

    private int statusCode;

    private int businessErrorCode;

    private LocalDate timestamp;

    private String message;

    private String details;

}
