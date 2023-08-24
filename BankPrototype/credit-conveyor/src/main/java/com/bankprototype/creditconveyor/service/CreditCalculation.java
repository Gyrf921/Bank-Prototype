package com.bankprototype.creditconveyor.service;

import java.math.BigDecimal;

public interface CreditCalculation {

    BigDecimal calculationMonthlyPayment(BigDecimal loanAmount, BigDecimal monthlyInterestRate, Integer interestPeriodsTerm);

    BigDecimal calculationMonthlyInterestRate(BigDecimal customLoanRate);

    Integer calculationInterestPeriodsTerm(Integer term);

    BigDecimal calculationTotalAmount(BigDecimal monthlyPayment, Integer countMonth);
}
