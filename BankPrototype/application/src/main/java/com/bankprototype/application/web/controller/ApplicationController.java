package com.bankprototype.application.web.controller;

import com.bankprototype.application.service.PrescoringValidation;
import com.bankprototype.application.web.feign.DealFeignClient;

import com.bankprototype.application.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ApplicationController {

    private final DealFeignClient feignClient;
    private final PrescoringValidation prescoringValidation;

    @Operation(summary = "Calculate 4 loan offers and prescoring")
    @ApiResponses(value = { @ApiResponse(responseCode = "400", description = "Age less then 18"),
            @ApiResponse(responseCode = "500", description = "Validation failed for some argument") })
    @PostMapping("/application")
    public List<LoanOfferDTO> calculatePossibleLoanOffers(@Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("[calculationPossibleLoanOffers] >> loanApplicationRequestDTO: {}", loanApplicationRequestDTO);

        prescoringValidation.checkBirthdateValid(loanApplicationRequestDTO);

        List<LoanOfferDTO> listLoanOffers
                = feignClient.calculatePossibleLoanOffers(loanApplicationRequestDTO);

        log.info("[calculationPossibleLoanOffers] << result: {}", listLoanOffers);

        return listLoanOffers;

    }

    @Operation(summary = "Choose one of the loan offers")
    @PostMapping("/application/offer")
    public void chooseOneOfTheOffers(@Valid @RequestBody LoanOfferDTO loanOfferDTO)
    {
        log.info("[chooseOneOfTheOffers] >> loanOfferDTO: {}", loanOfferDTO);

        feignClient.chooseOneOfTheOffers(loanOfferDTO);

        log.info("[chooseOneOfTheOffers] << result is void");

    }
}
