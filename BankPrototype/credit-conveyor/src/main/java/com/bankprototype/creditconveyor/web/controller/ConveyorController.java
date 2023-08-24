package com.bankprototype.creditconveyor.web.controller;

import com.bankprototype.creditconveyor.service.PrescoringCalculation;
import com.bankprototype.creditconveyor.service.ScoringCalculation;
import com.bankprototype.creditconveyor.web.dto.CreditDTO;
import com.bankprototype.creditconveyor.web.dto.LoanApplicationRequestDTO;
import com.bankprototype.creditconveyor.web.dto.LoanOfferDTO;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/conveyor")
@RequiredArgsConstructor
public class ConveyorController {

    private final PrescoringCalculation prescoringCalculation;
    private final ScoringCalculation scoringCalculation;

    @Operation(summary = "Calculate 4 loan offers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan Offers have been calculated"),
            @ApiResponse(responseCode = "400", description = "Validation failed for some argument. Invalid input supplied"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping("/offers")
    public List<LoanOfferDTO> calculatePossibleLoanOffers(@Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("[calculationPossibleLoanOffers] >> loanApplicationRequestDTO: {}", loanApplicationRequestDTO);

        List<LoanOfferDTO> loanOfferDTOs = prescoringCalculation.createListLoanOffer(loanApplicationRequestDTO);

        log.info("[calculationPossibleLoanOffers] << result: {}", loanOfferDTOs);

        return loanOfferDTOs;

    }

    @Operation(summary = "Calculate full loan parameters for credit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan parameters for credit has been calculated"),
            @ApiResponse(responseCode = "400", description = "Validation failed for some argument. Invalid input supplied"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping("/calculation")
    public CreditDTO calculateFullLoanParameters(@Valid @RequestBody ScoringDataDTO scoringDataDTO) {
        log.info("[fullCalculationLoanParameters] >> scoringDataDTO: {}", scoringDataDTO);

        CreditDTO creditDTO = scoringCalculation.createCredit(scoringDataDTO);

        log.info("[fullCalculationLoanParameters] << result: {}", creditDTO);

        return creditDTO;
    }

}
