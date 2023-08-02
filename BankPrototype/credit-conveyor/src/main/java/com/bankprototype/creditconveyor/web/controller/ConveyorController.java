package com.bankprototype.creditconveyor.web.controller;

import com.bankprototype.creditconveyor.service.PrescoringCalculation;
import com.bankprototype.creditconveyor.service.ScoringCalculation;
import com.bankprototype.creditconveyor.web.dto.CreditDTO;
import com.bankprototype.creditconveyor.web.dto.LoanApplicationRequestDTO;
import com.bankprototype.creditconveyor.web.dto.LoanOfferDTO;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/conveyor")
public class ConveyorController {

    private final PrescoringCalculation prescoringCalculation;
    private final ScoringCalculation scoringCalculation;

    public ConveyorController(PrescoringCalculation prescoringCalculation, ScoringCalculation scoringCalculation) {
        this.prescoringCalculation = prescoringCalculation;
        this.scoringCalculation = scoringCalculation;
    }

    @PostMapping("/offers")
    public List<LoanOfferDTO> calculatePossibleLoanOffers(@Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO)
    {
        log.info("[calculationPossibleLoanOffers] >> loanApplicationRequestDTO: {}", loanApplicationRequestDTO);

        List<LoanOfferDTO> loanOfferDTOs = prescoringCalculation.createListLoanOffer(loanApplicationRequestDTO);

        log.info("[calculationPossibleLoanOffers] << result: {}", loanOfferDTOs);

        return loanOfferDTOs;

    }

    @PostMapping("/calculation")
    public CreditDTO calculateFullLoanParameters(@Valid @RequestBody ScoringDataDTO scoringDataDTO)
    {
        log.info("[fullCalculationLoanParameters] >> scoringDataDTO: {}", scoringDataDTO);

        CreditDTO creditDTO = scoringCalculation.createCredit(scoringDataDTO);

        log.info("[fullCalculationLoanParameters] << result: {}", creditDTO);

        return creditDTO;
    }

}
