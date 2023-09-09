package com.bankprototype.application.web.controller;

import com.bankprototype.application.service.PrescoringValidation;
import com.bankprototype.application.web.dto.LoanApplicationRequestDTO;
import com.bankprototype.application.web.dto.LoanOfferDTO;
import com.bankprototype.application.web.feign.DealFeignClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ApplicationController {

    private final DealFeignClient feignClient;

    private final PrescoringValidation prescoringValidation;

    @Operation(summary = "Calculate 4 loan offers and prescoring")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Possible Loan Offers have been calculated"),
            @ApiResponse(responseCode = "400", description = "Validation failed for some argument"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping("/application")
    public ResponseEntity<List<LoanOfferDTO>> calculatePossibleLoanOffers(@Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("[calculationPossibleLoanOffers] >> loanApplicationRequestDTO: {}", loanApplicationRequestDTO);

        prescoringValidation.checkBirthdateValid(loanApplicationRequestDTO);

        List<LoanOfferDTO> listLoanOffers
                = feignClient.calculatePossibleLoanOffers(loanApplicationRequestDTO).getBody();

        log.info("[calculationPossibleLoanOffers] << result: {}", listLoanOffers);

        return ResponseEntity.ok().body(listLoanOffers);

    }

    @Operation(summary = "Choose one of the loan offers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan Offer have been choose"),
            @ApiResponse(responseCode = "400", description = "Validation failed for some argument"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping("/application/offer")
    public void chooseOneOfTheOffers(@Valid @RequestBody LoanOfferDTO loanOfferDTO) {
        log.info("[chooseOneOfTheOffers] >> loanOfferDTO: {}", loanOfferDTO);

        feignClient.chooseOneOfTheOffers(loanOfferDTO);

        log.info("[chooseOneOfTheOffers] << result is void, Loan Offer have been choose.");

    }
}
