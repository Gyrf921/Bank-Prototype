package com.bankprototype.gateway.web.feign;

import com.bankprototype.gateway.web.dto.LoanApplicationRequestDTO;
import com.bankprototype.gateway.web.dto.LoanOfferDTO;
import com.bankprototype.gateway.web.feign.fallback.ApplicationFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@FeignClient(value = "application", url = "http://application:8082", fallbackFactory = ApplicationFeignClientFallbackFactory.class)
public interface ApplicationFeignClient {

    @PostMapping
    ResponseEntity<List<LoanOfferDTO>> calculatePossibleLoanOffers(@Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO);

    @PostMapping("/offer")
    void chooseOneOfTheOffers(@Valid @RequestBody LoanOfferDTO loanOfferDTO);

}
