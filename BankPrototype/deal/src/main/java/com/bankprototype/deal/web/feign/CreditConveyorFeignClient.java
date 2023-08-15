package com.bankprototype.deal.web.feign;

import com.bankprototype.deal.web.dto.*;
import com.bankprototype.deal.exception.CreditConveyorFeignClientFallbackFactory;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "creditConveyor", url = "http://localhost:8080/conveyor") //, fallbackFactory = CreditConveyorFeignClientFallbackFactory.class
public interface CreditConveyorFeignClient {

    @PostMapping("/offers")
    List<LoanOfferDTO> calculatePossibleLoanOffers(@Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO);

    @PostMapping("/calculation")
    CreditDTO calculateFullLoanParameters(@Valid @RequestBody ScoringDataDTO scoringDataDTO);
}
