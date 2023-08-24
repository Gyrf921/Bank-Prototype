package com.bankprototype.creditconveyor.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CreditCalculationImplTest {

    @InjectMocks
    private CreditCalculationImpl calculation;

    @Test
    void calculationMonthlyPayment() {
        BigDecimal loanAmount = BigDecimal.valueOf(1000000);
        BigDecimal monthlyInterestRate = BigDecimal.valueOf(0.00417);
        Integer interestPeriodsTerm = -72;

        BigDecimal answer = BigDecimal.valueOf(16106);

        BigDecimal monthlyPayment = calculation.calculationMonthlyPayment(loanAmount, monthlyInterestRate, interestPeriodsTerm);

        System.out.println(answer);
        System.out.println(monthlyPayment);

        assertThat(monthlyPayment.subtract(answer).abs()).isLessThanOrEqualTo(BigDecimal.valueOf(1.0));
    }

    @Test
    void calculationMonthlyInterestRate() {
        BigDecimal customLoanRate = BigDecimal.valueOf(5.0000000);
        BigDecimal answer = BigDecimal.valueOf(0.00417);

        BigDecimal interestRate = calculation.calculationMonthlyInterestRate(customLoanRate);

        System.out.println(answer);
        System.out.println(interestRate);
        assertEquals(interestRate, answer);
    }

    @Test
    void calculationInterestPeriodsTerm() {

        Integer answer = -72;

        Integer interestPeriodsTerm = calculation.calculationInterestPeriodsTerm(6);


        System.out.println(answer);
        System.out.println(interestPeriodsTerm);
        assertEquals(interestPeriodsTerm, answer);
    }

    @Test
    void calculationTotalAmount() {
        BigDecimal answer = new BigDecimal(720000);

        BigDecimal totalAmount = calculation.calculationTotalAmount(new BigDecimal(10000), 72);

        System.out.println(answer);
        System.out.println(totalAmount);
        assertEquals(totalAmount, answer);
    }
}