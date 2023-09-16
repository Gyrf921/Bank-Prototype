package com.bankprototype.gateway.web.feign.fallback;

import com.bankprototype.gateway.exception.ErrorDetails;
import com.bankprototype.gateway.exception.ExternalException;
import com.bankprototype.gateway.web.dto.FinishRegistrationRequestDTO;
import com.bankprototype.gateway.web.feign.DealFeignClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;

@Component
@Slf4j
public class DealFeignClientFallbackFactory implements FallbackFactory<DealFeignClient> {
    @Override
    public DealFeignClient create(Throwable cause) {

        log.error("An exception occurred when calling the DealFeignClient", cause);

        return new DealFeignClient() {
            @Override
            public void completionRegistrationAndCalculateFullCredit(Long applicationId, FinishRegistrationRequestDTO requestDTO) {
                log.info("[Fallback.completionRegistrationAndCalculateFullCredit] >> applicationId: {}, requestDTO: {}", applicationId, requestDTO);
                getExternalException(cause);
                log.error("An exception occurred when calling the completionRegistrationAndCalculateFullCredit method", cause);

            }

            @Override
            public void sendDocuments(Long applicationId) {
                log.info("[Fallback.sendDocuments] >> applicationId: {}", applicationId);
                getExternalException(cause);
                log.error("An exception occurred when calling the sendDocuments method", cause);
            }

            @Override
            public void signDocuments(Long applicationId) {
                log.info("[Fallback.signDocuments] >> applicationId: {}", applicationId);
                getExternalException(cause);
                log.error("An exception occurred when calling the signDocuments method", cause);
            }

            @Override
            public void codeDocuments(Long sesCode, Long applicationId) {
                log.info("[Fallback.codeDocuments] >> applicationId: {}", applicationId);
                getExternalException(cause);
                log.error("An exception occurred when calling the codeDocuments method", cause);
            }

        };
    }

    private void getExternalException(Throwable cause) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        ErrorDetails errorDetails = null;
        if (cause instanceof FeignException) {
            FeignException feignException = (FeignException) cause;
            ByteBuffer buffer = feignException.responseBody().get();
            try {
                errorDetails = mapper.readValue(buffer.array(), ErrorDetails.class);
            } catch (IOException e) {
                throw new ExternalException(500, null, "Some error with server");
            }

            throw new ExternalException(errorDetails.getStatusCode(), errorDetails, errorDetails.getMessage());
        }
    }
}