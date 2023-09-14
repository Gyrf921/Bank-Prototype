package com.bankprototype.gateway.web.feign.fallback;

import com.bankprototype.gateway.exception.ErrorDetails;
import com.bankprototype.gateway.exception.ExternalException;
import com.bankprototype.gateway.web.dto.LoanApplicationRequestDTO;
import com.bankprototype.gateway.web.dto.LoanOfferDTO;
import com.bankprototype.gateway.web.feign.ApplicationFeignClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

@Component
@Slf4j
public class ApplicationFeignClientFallbackFactory implements FallbackFactory<ApplicationFeignClient> {

    @Override
    public ApplicationFeignClient create(Throwable cause) {

        log.error("An exception occurred when calling the ApplicationFeignClient ", cause);

        return new ApplicationFeignClient() {

            @Override
            public ResponseEntity<List<LoanOfferDTO>> calculatePossibleLoanOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {
                log.info("[Fallback.calculatePossibleLoanOffers] >> loanApplicationRequestDTO: {}", loanApplicationRequestDTO);

                getExternalException(cause);

                log.info("[Fallback.calculatePossibleLoanOffers] << result: null");

                return ResponseEntity.internalServerError().body(null);
            }

            @Override
            public void chooseOneOfTheOffers(LoanOfferDTO loanOfferDTO) {
                log.info("[Fallback.chooseOneOfTheOffers] >> LoanOfferDTO: {}", loanOfferDTO);

                getExternalException(cause);

                log.error("An exception occurred when calling the chooseOneOfTheOffers method", cause);
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
