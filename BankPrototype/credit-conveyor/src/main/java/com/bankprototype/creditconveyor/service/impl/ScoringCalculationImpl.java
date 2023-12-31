package com.bankprototype.creditconveyor.service.impl;

import com.bankprototype.creditconveyor.rule.RateRuleEngine;
import com.bankprototype.creditconveyor.service.CreditCalculation;
import com.bankprototype.creditconveyor.service.ScoringCalculation;
import com.bankprototype.creditconveyor.web.dto.CreditDTO;
import com.bankprototype.creditconveyor.web.dto.PaymentScheduleElement;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScoringCalculationImpl implements ScoringCalculation {

    private final CreditCalculation calculation;

    private final RateRuleEngine ruleEngine;

    @Value("${loanRate}")
    private Double loanRate;

    private static final int COUNT_MONTH_OF_YEAR = 12;

    @Override
    public CreditDTO createCredit(ScoringDataDTO scoringDataDTO) {

        log.info("[createCredit] >> scoringDataDTO: {}", scoringDataDTO);

        BigDecimal creditRate = ruleEngine.getRate(scoringDataDTO, BigDecimal.valueOf(loanRate));

        if (creditRate.compareTo(BigDecimal.ZERO) <= 0) {
            creditRate = BigDecimal.ONE;
        }

        CreditDTO creditDTO = calculationCredit(scoringDataDTO, creditRate);

        log.info("[createCredit] << result: {}", creditDTO);

        return creditDTO;
    }

    private CreditDTO calculationCredit(ScoringDataDTO scoringDataDTO, BigDecimal creditRate) {
        log.info("[calculationCredit] >> scoringDataDTO: {}, creditRate: {}", scoringDataDTO, creditRate);

        BigDecimal monthlyInterestRate = calculation.calculationMonthlyInterestRate(creditRate);
        Integer interestPeriodsTerm = calculation.calculationInterestPeriodsTerm(scoringDataDTO.getTerm());

        BigDecimal monthlyPaymentCalc = calculation.calculationMonthlyPayment(scoringDataDTO.getAmount(), monthlyInterestRate, interestPeriodsTerm);
        BigDecimal pskCalc = calculation.calculationTotalAmount(monthlyPaymentCalc, scoringDataDTO.getTerm() * COUNT_MONTH_OF_YEAR);

        CreditDTO creditDTO = CreditDTO.builder()
                .amount(scoringDataDTO.getAmount())
                .term(scoringDataDTO.getTerm())
                .monthlyPayment(monthlyPaymentCalc)
                .rate(creditRate)
                .psk(pskCalc)
                .isInsuranceEnabled(scoringDataDTO.getIsInsuranceEnabled())
                .isSalaryClient(scoringDataDTO.getIsSalaryClient())
                .paymentSchedule(calculationPaymentSchedules(monthlyPaymentCalc, pskCalc, creditRate))
                .build();

        log.info("[calculationCredit] << result: {}", creditDTO);
        return creditDTO;
    }

    private List<PaymentScheduleElement> calculationPaymentSchedules(BigDecimal monthlyPaymentCalc, BigDecimal pskCalc, BigDecimal rate) {
        log.info("[calculationPaymentSchedules] >> monthlyPaymentCalc: {}, pskCalc: {}, rate: {}", monthlyPaymentCalc, pskCalc, rate);

        List<PaymentScheduleElement> elementList = new LinkedList<>();
        int number = 0;
        LocalDate localDate = LocalDate.now();
        BigDecimal remainingDebt = pskCalc;
        BigDecimal scale = BigDecimal.valueOf(0.00001);

        BigDecimal monthRate = rate.multiply(BigDecimal.valueOf(0.01).divide(BigDecimal.valueOf(12), scale.scale(), RoundingMode.HALF_UP));

        BigDecimal interestPayment = remainingDebt.multiply(monthRate);

        do {
            number++;

            elementList.add(
                    PaymentScheduleElement.builder()
                            .number(number)
                            .date(localDate.plusMonths(number - 1L))
                            .totalPayment(monthlyPaymentCalc)
                            .interestPayment(interestPayment)
                            .debtPayment(monthlyPaymentCalc.subtract(interestPayment))
                            .remainingDebt(remainingDebt.subtract(monthlyPaymentCalc))
                            .build());

            remainingDebt = remainingDebt.subtract(monthlyPaymentCalc);
            interestPayment = remainingDebt.multiply(monthRate);
        }
        while (monthlyPaymentCalc.compareTo(remainingDebt) < 0);
        number++;
        elementList.add(
                PaymentScheduleElement.builder()
                        .number(number)
                        .date(localDate.plusMonths(number - 1L))
                        .totalPayment(remainingDebt)
                        .interestPayment(interestPayment)
                        .debtPayment(remainingDebt.subtract(interestPayment))
                        .remainingDebt(BigDecimal.ZERO)
                        .build());

        log.info("[calculationPaymentSchedules] << result: {}", elementList);
        return elementList;
    }

}
