package com.bankprototype.gateway.web.feign.fallback;

import com.bankprototype.gateway.web.dto.LoanApplicationRequestDTO;
import com.bankprototype.gateway.web.dto.LoanOfferDTO;
import com.bankprototype.gateway.web.feign.ApplicationFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

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

                log.info("[Fallback.calculatePossibleLoanOffers] << result: null");

                return ResponseEntity.internalServerError().body(null);
            }

            @Override
            public void chooseOneOfTheOffers(LoanOfferDTO loanOfferDTO) {
                log.info("[Fallback.chooseOneOfTheOffers] >> LoanOfferDTO: {}", loanOfferDTO);

                log.error("An exception occurred when calling the chooseOneOfTheOffers method", cause);
            }
        };
    }
}
