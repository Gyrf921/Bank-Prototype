package com.bankprototype.deal.web.feign.fallback;

import com.bankprototype.deal.exception.BadScoringInfoException;
import com.bankprototype.deal.exception.ExternalException;
import com.bankprototype.deal.exception.global.ConstantErrorCode;
import com.bankprototype.deal.exception.global.ErrorDetails;
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

            log.info("[if|else] >> Verification is a valid exception");
            if (errorDetails.getBusinessErrorCode() == ConstantErrorCode.BUSINESS_ERROR_CODE_SCORING_INFO) {
                log.info("[getExternalException] << BadScoringInfoException is valid exception");
                throw new BadScoringInfoException(errorDetails.getMessage());
            }

            ExternalException externalException = new ExternalException(errorDetails.getStatusCode(), errorDetails, errorDetails.getMessage());
            log.info("[ExceptionHandlerUtil.getExternalException] << externalException: {}", externalException.getMessage());
            throw externalException;
        }
    }

}
