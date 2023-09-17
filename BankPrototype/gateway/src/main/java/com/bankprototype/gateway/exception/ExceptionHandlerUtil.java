package com.bankprototype.gateway.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;

@Component
@Slf4j
public class ExceptionHandlerUtil {
    private final ObjectMapper mapper;

    @Autowired
    public ExceptionHandlerUtil(@Qualifier("exceptionMapper") ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void getExternalException(Throwable cause) {
        log.info("[ExceptionHandlerUtil.getExternalException] >> throwable(cause)");
        ErrorDetails errorDetails = null;
        if (cause instanceof FeignException) {
            FeignException feignException = (FeignException) cause;
            ByteBuffer buffer = feignException.responseBody().get();
            try {
                log.info("[ExceptionHandlerUtil.getExternalException] >> try -> to getting ErrorDetails from the received exception");
                errorDetails = mapper.readValue(buffer.array(), ErrorDetails.class);
            } catch (IOException e) {
                log.error("[ExceptionHandlerUtil.getExternalException] catch -> Error getting ErrorDetails from the received exception: {}", e.getMessage());
                log.info("[ExceptionHandlerUtil.getExternalException] << ExternalException: Some error with server");
                throw new ExternalException(500, null, "Some error with server");
            }
            ExternalException externalException = new ExternalException(errorDetails.getStatusCode(), errorDetails, errorDetails.getMessage());
            log.info("[ExceptionHandlerUtil.getExternalException] << externalException: {}", externalException.getMessage());
            throw externalException;
        }
    }
}
