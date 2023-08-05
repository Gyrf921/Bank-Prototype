package com.bankprototype.creditconveyor.service.impl;

import com.bankprototype.creditconveyor.web.dto.LoanApplicationRequestDTO;
import com.bankprototype.creditconveyor.web.dto.LoanOfferDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrescoringCalculationImplTest {

    @Mock
    private CreditCalculationImpl calculation;

    @InjectMocks
    private PrescoringCalculationImpl prescoringCalculation;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(prescoringCalculation, "loanRate", 8.0);
        ReflectionTestUtils.setField(prescoringCalculation, "ratioOfInsuranceEnabled", 1.0);
        ReflectionTestUtils.setField(prescoringCalculation, "ratioOfSalaryClient", 2.0);
    }

    @Test
    void createListLoanOffer() {

        LoanApplicationRequestDTO LNR_DTO = LoanApplicationRequestDTO.builder()
                .amount(BigDecimal.valueOf(1000000))
                .term(6)
                .build();

        BigDecimal requestAmountInsTrue = BigDecimal.valueOf(1100000);

        when(calculation.calculationMonthlyInterestRate(any()))
                .thenReturn(BigDecimal.valueOf(0.00417));
        when(calculation.calculationInterestPeriodsTerm(any()))
                .thenReturn(-72);
        when(calculation.calculationMonthlyPayment(any(), any(), any()))
                .thenReturn(BigDecimal.valueOf(16101));
        when(calculation.calculationTotalAmount(any(), any()))
                .thenReturn(BigDecimal.valueOf(1159272));

        List<LoanOfferDTO> loanOfferDTO = prescoringCalculation.createListLoanOffer(LNR_DTO);


        loanOfferDTO.forEach(System.out::println);
        assertEquals(loanOfferDTO.size(), 4);

        System.out.println(loanOfferDTO.get(0).getRequestedAmount());
        System.out.println(LNR_DTO.getAmount());
        assertEquals(loanOfferDTO.get(0).getRequestedAmount(), LNR_DTO.getAmount());

        System.out.println(loanOfferDTO.get(1).getRequestedAmount());
        System.out.println(requestAmountInsTrue);
        assertEquals(loanOfferDTO.get(1).getRequestedAmount(), requestAmountInsTrue);

        verify(calculation, times(4)).calculationMonthlyInterestRate(any());
        verify(calculation, times(4)).calculationInterestPeriodsTerm(any());
        verify(calculation, times(4)).calculationMonthlyPayment(any(), any(), any());
        verify(calculation, times(4)).calculationTotalAmount(any(), any());

    }


}