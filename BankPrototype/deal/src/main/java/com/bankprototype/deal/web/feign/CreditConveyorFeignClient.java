package com.bankprototype.deal.web.feign;

import com.bankprototype.deal.web.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@FeignClient(value = "creditConveyor", url = "http://localhost:8080/conveyor")
public interface CreditConveyorFeignClient {

    @PostMapping("/offers")
    List<LoanOfferDTO> calculatePossibleLoanOffers(@Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO);

    @PostMapping("/calculation")
    CreditDTO calculateFullLoanParameters(@Valid @RequestBody ScoringDataDTO scoringDataDTO);
}
