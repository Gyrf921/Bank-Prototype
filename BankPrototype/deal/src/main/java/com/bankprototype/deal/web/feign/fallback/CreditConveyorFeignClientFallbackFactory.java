package com.bankprototype.deal.web.feign.fallback;

import com.bankprototype.deal.exception.BadScoringInfoException;
import com.bankprototype.deal.exception.ExternalException;
import com.bankprototype.deal.exception.global.ConstantErrorCode;
import com.bankprototype.deal.exception.global.ErrorDetails;
import com.bankprototype.deal.service.DealProducer;
import com.bankprototype.deal.web.dto.CreditDTO;
import com.bankprototype.deal.web.dto.LoanApplicationRequestDTO;
import com.bankprototype.deal.web.dto.LoanOfferDTO;
import com.bankprototype.deal.web.dto.ScoringDataDTO;
import com.bankprototype.deal.web.feign.CreditConveyorFeignClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreditConveyorFeignClientFallbackFactory implements FallbackFactory<CreditConveyorFeignClient> {

    @Override
    public CreditConveyorFeignClient create(Throwable cause) {

        log.error("An exception occurred when calling the CreditConveyorFeignClient", cause);

        return new CreditConveyorFeignClient() {

            @Override
            public ResponseEntity<List<LoanOfferDTO>> calculatePossibleLoanOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {

                log.info("[Fallback.calculatePossibleLoanOffers] >> loanApplicationRequestDTO: {}", loanApplicationRequestDTO);

                getExternalException(cause);

                log.info("[Fallback.calculatePossibleLoanOffers] << result: null");

                return ResponseEntity.internalServerError().body(List.of());
            }

            @Override
            public ResponseEntity<CreditDTO> calculateFullLoanParameters(ScoringDataDTO scoringDataDTO) {
                log.info("[Fallback.calculateFullLoanParameters] >> scoringDataDTO: {}", scoringDataDTO);

                getExternalException(cause);

                log.info("[Fallback.calculateFullLoanParameters] << result: null");

                return ResponseEntity.internalServerError().body(null);
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
            if (errorDetails.getBusinessErrorCode() == ConstantErrorCode.BUSINESS_ERROR_CODE_SCORING_INFO){
                throw new BadScoringInfoException(errorDetails.getMessage());
            }
            throw new ExternalException(errorDetails.getStatusCode(), errorDetails, errorDetails.getMessage());
        }
    }

}
