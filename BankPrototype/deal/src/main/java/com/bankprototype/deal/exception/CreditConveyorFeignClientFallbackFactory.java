package com.bankprototype.deal.exception;

import com.bankprototype.deal.web.dto.CreditDTO;
import com.bankprototype.deal.web.dto.LoanApplicationRequestDTO;
import com.bankprototype.deal.web.dto.LoanOfferDTO;
import com.bankprototype.deal.web.dto.ScoringDataDTO;
import com.bankprototype.deal.web.feign.CreditConveyorFeignClient;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Component
@Slf4j
public class CreditConveyorFeignClientFallbackFactory implements FallbackFactory<CreditConveyorFeignClient> {
    private static final String FALLBACK_NAME = "CreditConveyorFallback";

    @Override
    public CreditConveyorFeignClient create(Throwable cause) {
        return new CreditConveyorFeignClient() {
            @Override
            public List<LoanOfferDTO> calculatePossibleLoanOffers(@Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
                log.error("{} is activated for calculatePossibleLoanOffers()", FALLBACK_NAME, cause);
                throw new RuntimeException();
            }

            @Override
            public CreditDTO calculateFullLoanParameters(ScoringDataDTO scoringDataDTO) {
                log.error("{} is activated for calculateFullLoanParameters()", FALLBACK_NAME, cause);
                throw new RuntimeException();
            }
        };
    }
}
