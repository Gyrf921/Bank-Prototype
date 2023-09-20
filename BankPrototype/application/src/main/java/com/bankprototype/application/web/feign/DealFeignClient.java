package com.bankprototype.application.web.feign;

import com.bankprototype.application.web.dto.FinishRegistrationRequestDTO;
import com.bankprototype.application.web.dto.LoanApplicationRequestDTO;
import com.bankprototype.application.web.dto.LoanOfferDTO;
import com.bankprototype.application.web.feign.fallback.DealFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@FeignClient(value = "deal", url = "http://deal:8081", fallbackFactory = DealFeignClientFallbackFactory.class)
public interface DealFeignClient {

    @PostMapping("/application")
    ResponseEntity<List<LoanOfferDTO>> calculatePossibleLoanOffers(@Valid @RequestBody LoanApplicationRequestDTO requestDTO);

    @PutMapping("/offer")
    void chooseOneOfTheOffers(@RequestBody LoanOfferDTO loanOfferDTO);

    @PostMapping("/calculate/{applicationId}")
    void completionRegistrationAndCalculateFullCredit(@PathVariable(value = "applicationId") Long applicationId,
                                                      @Valid @RequestBody FinishRegistrationRequestDTO requestDTO);
}
