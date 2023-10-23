package com.bankprototype.creditconveyor.service.impl;

import com.bankprototype.creditconveyor.web.dto.LoanApplicationRequestDTO;
import com.bankprototype.creditconveyor.web.dto.LoanOfferDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class PrescoringCalculationImplTest {

    @Autowired
    private PrescoringCalculationImpl prescoringCalculation;

    @Test
    void createListLoanOffer() {

        LoanApplicationRequestDTO LNR_DTO = LoanApplicationRequestDTO.builder()
                .amount(BigDecimal.valueOf(1000000))
                .term(6)
                .build();

        List<LoanOfferDTO> loanOfferDTO = prescoringCalculation.createListLoanOffer(LNR_DTO);

        loanOfferDTO.forEach(System.out::println);
        assertThat(loanOfferDTO.toArray()).hasSize(4);

        assertThat(loanOfferDTO.get(3).getTotalAmount()).isLessThan(loanOfferDTO.get(2).getTotalAmount());
        assertThat(loanOfferDTO.get(3).getIsSalaryClient()).isTrue();
        assertThat(loanOfferDTO.get(3).getIsInsuranceEnabled()).isTrue();

        assertThat(loanOfferDTO.get(0).getIsSalaryClient()).isFalse();
        assertThat(loanOfferDTO.get(0).getIsInsuranceEnabled()).isFalse();

        assertThat(loanOfferDTO.get(0).getMonthlyPayment()).isGreaterThan(loanOfferDTO.get(1).getMonthlyPayment());
        assertThat(loanOfferDTO.get(1).getMonthlyPayment()).isGreaterThan(loanOfferDTO.get(2).getMonthlyPayment());
        assertThat(loanOfferDTO.get(2).getMonthlyPayment()).isGreaterThan(loanOfferDTO.get(3).getMonthlyPayment());
    }


}