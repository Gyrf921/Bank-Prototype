package com.bankprototype.deal.web.feign;

import com.bankprototype.deal.web.dto.CreditDTO;
import com.bankprototype.deal.web.dto.LoanApplicationRequestDTO;
import com.bankprototype.deal.web.dto.LoanOfferDTO;
import com.bankprototype.deal.web.dto.ScoringDataDTO;
import com.bankprototype.deal.web.feign.fallback.CreditConveyorFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@FeignClient(value = "conveyor", url = "conveyor:8080/conveyor", fallbackFactory = CreditConveyorFeignClientFallbackFactory.class)
public interface CreditConveyorFeignClient {

    @PostMapping("/offers")
    ResponseEntity<List<LoanOfferDTO>> calculatePossibleLoanOffers(@Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO);

    @PostMapping("/calculation")
    ResponseEntity<CreditDTO> calculateFullLoanParameters(@Valid @RequestBody ScoringDataDTO scoringDataDTO);
}
