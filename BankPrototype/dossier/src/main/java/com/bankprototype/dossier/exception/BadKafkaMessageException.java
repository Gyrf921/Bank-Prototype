package com.bankprototype.dossier.exception;

public class BadKafkaMessageException extends RuntimeException {
    public BadKafkaMessageException(String message) {
        super(message);
    }
}