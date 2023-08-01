package com.bankprototype.creditconveyor.service.impl;

import com.bankprototype.creditconveyor.service.CreditCalculation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
public class CreditCalculationImpl implements CreditCalculation {

    private static final int COUNT_MONTH_OF_YEAR = 12;

    @Override
    public BigDecimal calculationMonthlyPayment(BigDecimal loanAmount, BigDecimal monthlyInterestRate, Integer interestPeriodsTerm) {
        log.info("[calculationMonthlyPaymentAmount] >> loanAmount: {}, monthlyInterestRate: {}, interestPeriodsTerm: {}", loanAmount,monthlyInterestRate,interestPeriodsTerm);

        BigDecimal tempSumForPow = BigDecimal.ONE.add(monthlyInterestRate);
        BigDecimal temp = tempSumForPow.pow(Math.abs(interestPeriodsTerm));
        BigDecimal tempPow = BigDecimal.ONE.divide(temp, temp.scale(), RoundingMode.DOWN);
        BigDecimal tempForCalc = BigDecimal.ONE.subtract(tempPow);

        BigDecimal monthlyPaymentAmount = loanAmount.multiply(monthlyInterestRate).divide(tempForCalc, RoundingMode.valueOf(5));

        log.info("[calculationMonthlyPaymentAmount] << result: {}", monthlyPaymentAmount);

        return monthlyPaymentAmount;
    }

    @Override
    public BigDecimal calculationMonthlyInterestRate(BigDecimal customLoanRate) {
        log.info("[calculationMonthlyInterestRate] >> customLoanRate: {}", customLoanRate);

        BigDecimal denominator = BigDecimal.valueOf(100 * COUNT_MONTH_OF_YEAR);

        BigDecimal scale = new BigDecimal("0.00001");

        BigDecimal monthlyInterestRate = customLoanRate.divide(denominator, scale.scale(), RoundingMode.HALF_EVEN);

        log.info("[calculationMonthlyInterestRate] << result: {}", monthlyInterestRate);

        return monthlyInterestRate;
    }

    @Override
    public Integer calculationInterestPeriodsTerm(Integer term) {
        log.info("[calculationInterestPeriodsTerm] >> term: {}, static countMonthOfYear: {}", term, COUNT_MONTH_OF_YEAR);

        Integer interestPeriodsTerm = (COUNT_MONTH_OF_YEAR * term) * -1;

        log.info("[calculationInterestPeriodsTerm] << result: {}", interestPeriodsTerm);

        return interestPeriodsTerm;
    }

    @Override
    public BigDecimal calculationTotalAmount(BigDecimal monthlyPayment, Integer countMonth) {
        log.info("[calculationTotalAmount] >> monthlyPayment: {}, countMonth: {}", monthlyPayment, countMonth);

        BigDecimal totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(countMonth));

        log.info("[calculationTotalAmount] << result: {}", totalAmount);

        return totalAmount;
    }
}
