package com.bankprototype.creditconveyor.service.impl;

import com.bankprototype.creditconveyor.service.CreditCalculation;
import com.bankprototype.creditconveyor.service.PrescoringCalculation;
import com.bankprototype.creditconveyor.web.dto.LoanApplicationRequestDTO;
import com.bankprototype.creditconveyor.web.dto.LoanOfferDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrescoringCalculationImpl implements PrescoringCalculation {

    private final CreditCalculation calculation;

    @Value("${loanRate}") //8
    private Double loanRate;

    @Value("${ratioOfInsuranceEnabled}")
    private Double ratioOfInsuranceEnabled;

    @Value("${ratioOfSalaryClient}")
    private Double ratioOfSalaryClient;

    private static final BigDecimal COST_INSURANCE = BigDecimal.valueOf(100000);
    private static final int COUNT_MONTH_OF_YEAR = 12;



    @Override
    public List<LoanOfferDTO> createListLoanOffer(LoanApplicationRequestDTO requestDTO){

        log.info("[createLoanOffer] >> requestDTO (loanApplicationRequestDTO): {}", requestDTO);
        BigDecimal creditRatio = BigDecimal.valueOf(loanRate);
        List<LoanOfferDTO> loanOfferDTOs = new LinkedList<>();

        loanOfferDTOs.add(calculateLoanOffer(1L ,requestDTO.getAmount(), requestDTO.getTerm(),
                creditRatio.subtract(BigDecimal.valueOf(ratioOfInsuranceEnabled + ratioOfSalaryClient)), false, false));

        loanOfferDTOs.add(calculateLoanOffer(2L ,requestDTO.getAmount().add(COST_INSURANCE), requestDTO.getTerm(),
                creditRatio.subtract(BigDecimal.valueOf(ratioOfInsuranceEnabled)), true, false));

        loanOfferDTOs.add(calculateLoanOffer(3L ,requestDTO.getAmount(), requestDTO.getTerm(),
                creditRatio.subtract(BigDecimal.valueOf(ratioOfSalaryClient)), false, true));

        loanOfferDTOs.add(calculateLoanOffer(4L ,requestDTO.getAmount().add(COST_INSURANCE), requestDTO.getTerm(),
                creditRatio, true, true));



        log.info("[createLoanOffer] << result: {}", loanOfferDTOs);

        return loanOfferDTOs;
    }



    private LoanOfferDTO calculateLoanOffer(Long applicationId, BigDecimal loanAmount, Integer termInYears,
                                            BigDecimal creditRatio, Boolean isInsuranceEnabled, Boolean isSalaryClient) {

        log.info("[calculationMonthlyPaymentAmount] >> applicationId: {}, loanAmount: {}, termInYears: {}, creditRatio: {}, isInsuranceEnabled: {}, isSalaryClient: {}"
                ,applicationId, loanAmount, termInYears, creditRatio, isInsuranceEnabled, isSalaryClient);


        BigDecimal monthlyInterestRate = calculation.calculationMonthlyInterestRate(creditRatio);
        Integer interestPeriodsTerm = calculation.calculationInterestPeriodsTerm(termInYears);
        BigDecimal monthlyPaymentAmount = calculation.calculationMonthlyPayment(loanAmount, monthlyInterestRate, interestPeriodsTerm);
        BigDecimal totalAmountCalc = calculation.calculationTotalAmount(monthlyPaymentAmount, termInYears* COUNT_MONTH_OF_YEAR);


        LoanOfferDTO loanOfferDTO = LoanOfferDTO.builder()
                .applicationId(applicationId)
                .requestedAmount(loanAmount)
                .totalAmount(totalAmountCalc.setScale(0, RoundingMode.HALF_UP))
                .term(termInYears)
                .monthlyPayment(monthlyPaymentAmount.setScale(0, RoundingMode.HALF_UP))
                .rate(creditRatio)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .build();

        log.info("[calculationMonthlyPaymentAmount] << result: {}", loanOfferDTO);

        return loanOfferDTO;
    }


}
