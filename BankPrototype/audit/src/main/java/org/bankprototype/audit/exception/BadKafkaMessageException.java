package org.bankprototype.audit.exception;

public class BadKafkaMessageException extends RuntimeException {
    public BadKafkaMessageException(String message) {
        super(message);
    }
}
