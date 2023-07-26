package com.bankprototype.creditconveyor.service.impl;

import com.bankprototype.creditconveyor.service.ICreditCalculation;
import com.bankprototype.creditconveyor.service.IScoringCalculation;
import com.bankprototype.creditconveyor.service.rule.RateRuleEngine;
import com.bankprototype.creditconveyor.service.rule.impl.person.*;
import com.bankprototype.creditconveyor.service.rule.impl.work.*;
import com.bankprototype.creditconveyor.web.dto.CreditDTO;
import com.bankprototype.creditconveyor.web.dto.LoanOfferDTO;
import com.bankprototype.creditconveyor.web.dto.PaymentScheduleElement;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class ScoringCalculationImpl implements IScoringCalculation {

    private final ICreditCalculation calculation;


    //TODO сделать нормальный ввод, потому что Value от spring не внедряет данные
    private static Double loanRate = 8.0;

    private static Double ratioOfInsuranceEnabled = 1.0;

    //@Value("${credit-conveyor.service.ratio-of-salary-client}")
    private static Double ratioOfSalaryClient = 2.0;

    private static final int countMonthOfYear = 12;
    public ScoringCalculationImpl(CreditCalculationImpl calculation) {
        this.calculation = calculation;
    }

    @Override
    public CreditDTO createCredit(ScoringDataDTO scoringDataDTO) {

        log.info("[createCredit] >> scoringDataDTO: {}", scoringDataDTO);

        RateRuleEngine ruleEngine = new RateRuleEngine(List.of(
                new BirthdateRateRule(),
                new DependentAmountRateRule(),
                new GenderRateRule(),
                new InsuranceRateRule(),
                new MaritalStatusRateRule(),
                new SalaryClientRateRule(),
                new CurrentWorkExperienceRateRule(),
                new EmploymentStatusRateRule(),
                new SalaryRateRule(),
                new TotalWorkExperienceRateRule(),
                new WorkPositionRateRule()
        )); // Все правила проверяем

        BigDecimal creditRate = ruleEngine.getRate(scoringDataDTO, BigDecimal.valueOf(loanRate));

        CreditDTO creditDTO;
        if(creditRate.equals(BigDecimal.ZERO)) {
            creditDTO = calculationCredit(scoringDataDTO, BigDecimal.ONE);
        }
        else if (BigDecimal.valueOf(1000000).compareTo(creditRate) < 0){
            creditDTO = null;
        }
        else{
            creditDTO = calculationCredit(scoringDataDTO, creditRate);
        }

        log.info("[createCredit] << result: {}", creditDTO);

        return creditDTO;
    }

    private CreditDTO calculationCredit(ScoringDataDTO scoringDataDTO, BigDecimal creditRate) {
        log.info("[calculationCredit] >> scoringDataDTO: {}, creditRate: {}", scoringDataDTO, creditRate);

        BigDecimal monthlyInterestRate = calculation.calculationMonthlyInterestRate(creditRate);
        Integer interestPeriodsTerm = calculation.calculationInterestPeriodsTerm(scoringDataDTO.getTerm());

        BigDecimal monthlyPaymentCalc = calculation.calculationMonthlyPayment(scoringDataDTO.getAmount(), monthlyInterestRate, interestPeriodsTerm);
        BigDecimal pskCalc = calculation.calculationTotalAmount(monthlyPaymentCalc, scoringDataDTO.getTerm() * countMonthOfYear);

        CreditDTO creditDTO = CreditDTO.builder()
                .amount(scoringDataDTO.getAmount())
                .term(scoringDataDTO.getTerm())
                .monthlyPayment(monthlyPaymentCalc)
                .rate(creditRate)
                .psk(pskCalc)
                .isInsuranceEnabled(scoringDataDTO.getIsInsuranceEnabled())
                .isSalaryClient(scoringDataDTO.getIsInsuranceEnabled())
                .paymentSchedule(calculationPaymentSchedules(monthlyPaymentCalc, pskCalc, creditRate))
                .build();

        log.info("[calculationCredit] << result: {}", creditDTO);
        return creditDTO;
    }

    private List<PaymentScheduleElement> calculationPaymentSchedules(BigDecimal monthlyPaymentCalc, BigDecimal pskCalc, BigDecimal rate) {

        List<PaymentScheduleElement> elementList = new LinkedList<>();
        int number = 0;
        LocalDate localDate = LocalDate.now();
        BigDecimal remainingDebt = pskCalc;//оставшийся долг;
        BigDecimal scale = BigDecimal.valueOf(0.00001);

        BigDecimal monthRate = rate.multiply(BigDecimal.valueOf(0.01).divide(BigDecimal.valueOf(12), scale.scale(), RoundingMode.HALF_UP));

        BigDecimal interestPayment = remainingDebt.multiply(monthRate); //выплата процентов;

        do {
            number++;

            elementList.add(
                    PaymentScheduleElement.builder()
                            .number(number)
                            .date(localDate.plusMonths(number-1))
                            .totalPayment(monthlyPaymentCalc)
                            .interestPayment(interestPayment)
                            .debtPayment(monthlyPaymentCalc.subtract(interestPayment)) //выплата долга;
                            .remainingDebt(remainingDebt.subtract(monthlyPaymentCalc))
                            .build());

            remainingDebt = remainingDebt.subtract(monthlyPaymentCalc);
            interestPayment = remainingDebt.multiply(monthRate);
        }
        while (monthlyPaymentCalc.compareTo(remainingDebt) < 0);
        //remainingDebt(100) < monthlyPaymentCalc(16107) but not zero
        number++;
        elementList.add(
                PaymentScheduleElement.builder()
                        .number(number)
                        .date(localDate.plusMonths(number-1))
                        .totalPayment(remainingDebt)
                        .interestPayment(interestPayment)
                        .debtPayment(remainingDebt.subtract(interestPayment)) //выплата долга;
                        .remainingDebt(BigDecimal.ZERO)
                        .build());

        log.info("[calculationPaymentSchedules] << result: {}", elementList);
        return elementList;
    }

}
