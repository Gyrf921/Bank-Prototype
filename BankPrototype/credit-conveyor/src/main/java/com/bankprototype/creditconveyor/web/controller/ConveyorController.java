package com.bankprototype.creditconveyor.web.controller;

import com.bankprototype.creditconveyor.service.IPrescoringCalculation;
import com.bankprototype.creditconveyor.web.dto.CreditDTO;
import com.bankprototype.creditconveyor.web.dto.LoanApplicationRequestDTO;
import com.bankprototype.creditconveyor.web.dto.LoanOfferDTO;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/conveyor")
public class ConveyorController {

    private final IPrescoringCalculation prescoringCalculation;

    public ConveyorController(IPrescoringCalculation prescoringCalculation) {
        this.prescoringCalculation = prescoringCalculation;
    }

    @PostMapping("/offers")
    public List<LoanOfferDTO> getInfoAboutMeeting(@Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO)
    {
        log.info("[getInfoAboutMeeting] >> loanApplicationRequestDTO: {}", loanApplicationRequestDTO);

        List<LoanOfferDTO> loanOfferDTOs = prescoringCalculation.createListLoanOffer(loanApplicationRequestDTO);

        log.info("[getInfoAboutMeeting] << result: {}", loanOfferDTOs);

        return loanOfferDTOs;

    }

    @PostMapping("/calculation")
    public CreditDTO createMeeting(@Valid @RequestBody ScoringDataDTO scoringDataDTO)
    {
        //TODO валидация присланных данных + скоринг данных + полный расчет параметров кредита

        return null;
    }

}
