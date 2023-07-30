package com.bankprototype.creditconveyor.service.impl;

import com.bankprototype.creditconveyor.rule.RateRuleEngine;
import com.bankprototype.creditconveyor.web.dto.CreditDTO;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScoringCalculationImplTest {
    @Mock
    private CreditCalculationImpl calculation;

    @Mock
    private RateRuleEngine ruleEngine;

    @InjectMocks
    private ScoringCalculationImpl scoringCalculation;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(scoringCalculation, "loanRate", 5.0);
    }

    @Test
    void createCredit() {
        ScoringDataDTO scoringDataDTO = ScoringDataDTO.builder()
                .term(6)
                .amount(BigDecimal.valueOf(1000000))
                .build();

        when(ruleEngine.getRate(any(), any()))
                .thenReturn(BigDecimal.valueOf(5.0));

        BigDecimal monthlyInterestRate = BigDecimal.valueOf(0.00417);
        int interestPeriodsTerm = -72;
        BigDecimal monthlyPaymentCalc = BigDecimal.valueOf(16101);
        BigDecimal pskCalc = BigDecimal.valueOf(1159272);

        when(calculation.calculationMonthlyInterestRate(any()))
                .thenReturn(monthlyInterestRate);
        when(calculation.calculationInterestPeriodsTerm(any()))
                .thenReturn(interestPeriodsTerm);
        when(calculation.calculationMonthlyPayment(any(), any(), any()))
                .thenReturn(monthlyPaymentCalc);
        when(calculation.calculationTotalAmount(any(), any()))
                .thenReturn(pskCalc);

        CreditDTO creditDTO =  scoringCalculation.createCredit(scoringDataDTO);

        System.out.println(creditDTO);


        assertEquals(creditDTO.getPsk(), pskCalc);
        assertEquals(creditDTO.getMonthlyPayment(), monthlyPaymentCalc);

        Integer tempInPaymentSchedule = creditDTO.getPaymentSchedule().get(Math.abs(interestPeriodsTerm)-1).getDate().getYear() - creditDTO.getPaymentSchedule().get(0).getDate().getYear();
        assertEquals(creditDTO.getTerm(), tempInPaymentSchedule);

        assertEquals(creditDTO.getPaymentSchedule().size(), 72);
        assertEquals(creditDTO.getPaymentSchedule().get(0).getTotalPayment(), monthlyPaymentCalc);

        System.out.println("creditDTO.getPaymentSchedule().get(0).getInterestPayment() = " + creditDTO.getPaymentSchedule().get(0).getInterestPayment());
        System.out.println("creditDTO.getPaymentSchedule().get(10).getInterestPayment() = " + creditDTO.getPaymentSchedule().get(10).getInterestPayment());
        assertThat(creditDTO.getPaymentSchedule().get(0).getInterestPayment()).isNotEqualTo(creditDTO.getPaymentSchedule().get(10).getInterestPayment());

        verify(calculation, times(1)).calculationMonthlyInterestRate(any());
        verify(calculation, times(1)).calculationInterestPeriodsTerm(any());
        verify(calculation, times(1)).calculationMonthlyPayment(any(), any(), any());
        verify(calculation, times(1)).calculationTotalAmount(any(), any());

    }
}