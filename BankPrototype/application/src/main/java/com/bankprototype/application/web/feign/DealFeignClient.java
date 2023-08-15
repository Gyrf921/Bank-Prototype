package com.bankprototype.application.web.feign;

import com.bankprototype.application.web.dto.*;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "deal", url = "http://localhost:8081/deal")
public interface DealFeignClient {

    @PostMapping("/application")
    List<LoanOfferDTO> calculatePossibleLoanOffers(@Valid @RequestBody LoanApplicationRequestDTO requestDTO);

    @PutMapping("/offer")
    void chooseOneOfTheOffers(@RequestBody LoanOfferDTO loanOfferDTO);

    @PostMapping("/calculate/{applicationId}")
    void completionRegistrationAndCalculateFullCredit(@PathVariable(value = "applicationId") Long applicationId,
                                                             @Valid @RequestBody FinishRegistrationRequestDTO requestDTO);
}
