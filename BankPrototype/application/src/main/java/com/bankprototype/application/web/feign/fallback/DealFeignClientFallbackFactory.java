package com.bankprototype.application.web.feign.fallback;

import com.bankprototype.application.exception.ErrorDetails;
import com.bankprototype.application.exception.ExternalException;
import com.bankprototype.application.web.dto.FinishRegistrationRequestDTO;
import com.bankprototype.application.web.dto.LoanApplicationRequestDTO;
import com.bankprototype.application.web.dto.LoanOfferDTO;
import com.bankprototype.application.web.feign.DealFeignClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DealFeignClientFallbackFactory implements FallbackFactory<DealFeignClient> {
    @Override
    public DealFeignClient create(Throwable cause) {

        log.error("An exception occurred when calling the DealFeignClient", cause);

        return new DealFeignClient() {
            @Override
            public ResponseEntity<List<LoanOfferDTO>> calculatePossibleLoanOffers(LoanApplicationRequestDTO requestDTO) {

                log.info("[Fallback.calculatePossibleLoanOffers] >> loanApplicationRequestDTO: {}", requestDTO);

                getExternalException(cause);

                log.info("[Fallback.calculatePossibleLoanOffers] << result: null");

                return ResponseEntity.internalServerError().body(null);
            }

            @Override
            public void chooseOneOfTheOffers(LoanOfferDTO loanOfferDTO) {
                log.info("[Fallback.chooseOneOfTheOffers] >> loanOfferDTO: {}", loanOfferDTO);

                getExternalException(cause);

                log.error("An exception occurred when calling the chooseOneOfTheOffers method", cause);
            }

            @Override
            public void completionRegistrationAndCalculateFullCredit(Long applicationId, FinishRegistrationRequestDTO requestDTO) {
                log.info("[Fallback.completionRegistrationAndCalculateFullCredit] >> applicationId: {}, requestDTO: {}", applicationId, requestDTO);

                getExternalException(cause);

                log.error("An exception occurred when calling the completionRegistrationAndCalculateFullCredit method", cause);
            }
        };
    }

    private void getExternalException(Throwable cause) {
        log.info("[getExternalException] >> throwable(cause)");
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        ErrorDetails errorDetails = null;
        if (cause instanceof FeignException) {
            FeignException feignException = (FeignException) cause;
            ByteBuffer buffer = feignException.responseBody().get();
            try {
                log.info("[getExternalException] >> try -> to getting ErrorDetails from the received exception");
                errorDetails = mapper.readValue(buffer.array(), ErrorDetails.class);
            } catch (IOException e) {
                log.error("[getExternalException] catch -> Error getting ErrorDetails from the received exception: {}", e.getMessage());
                log.info("[getExternalException] << ExternalException: Some error with server");
                throw new ExternalException(500, null, "Some error with server");
            }

            ExternalException externalException = new ExternalException(errorDetails.getStatusCode(), errorDetails, errorDetails.getMessage());
            log.info("[ExceptionHandlerUtil.getExternalException] << externalException: {}", externalException.getMessage());
            throw externalException;
        }
    }
}
