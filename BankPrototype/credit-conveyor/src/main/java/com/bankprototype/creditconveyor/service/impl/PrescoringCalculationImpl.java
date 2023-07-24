package com.bankprototype.creditconveyor.service.impl;

import com.bankprototype.creditconveyor.web.dto.LoanApplicationRequestDTO;
import com.bankprototype.creditconveyor.web.dto.LoanOfferDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class PrescoringCalculationImpl{

    @Value("${credit-conveyor.service.loan-rate}")
    private static Double loanRate;

    @Value("${credit-conveyor.service.ratio-of-insurance-enabled}")
    private static Double ratioOfInsuranceEnabled;

    @Value("${credit-conveyor.service.ratio-of-salary-client}")
    private static Double ratioOfSalaryClient;

    private static final int countMonthOfYear = 12;


    public List<LoanOfferDTO> createLoanOffer(LoanApplicationRequestDTO LNR_DTO){
        log.info("[createLoanOffer] >> LNR_DTO (loanApplicationRequestDTO): {}", LNR_DTO);

        List<LoanOfferDTO> loanOfferDTOs = new LinkedList<>();

        loanOfferDTOs.add(calculationLoanOffer(LNR_DTO, true, true));

        loanOfferDTOs.add(calculationLoanOffer(LNR_DTO, false, true));

        loanOfferDTOs.add(calculationLoanOffer(LNR_DTO, true, false));

        loanOfferDTOs.add(calculationLoanOffer(LNR_DTO, false, false));

        log.info("[createLoanOffer] >> LNR_DTO (loanApplicationRequestDTO): {}", LNR_DTO);

        return loanOfferDTOs;
    }

    public LoanOfferDTO calculationLoanOffer(LoanApplicationRequestDTO LNR_DTO, Boolean isInsuranceEnabled, Boolean isSalaryClient){

        LoanOfferDTO loanOfferDTO;
        Double creditRatio = loanRate;


        if (isInsuranceEnabled & isSalaryClient){
            creditRatio -= (ratioOfInsuranceEnabled + ratioOfSalaryClient);
            loanOfferDTO = calculationMonthlyPaymentAmount(1L ,LNR_DTO.getAmount(), LNR_DTO.getTerm(), creditRatio, isInsuranceEnabled, isSalaryClient);
        }
        else if(!isInsuranceEnabled & isSalaryClient){
            creditRatio -= (ratioOfSalaryClient);
            loanOfferDTO = calculationMonthlyPaymentAmount(2L ,LNR_DTO.getAmount(), LNR_DTO.getTerm(), creditRatio, isInsuranceEnabled, isSalaryClient);
        }
        else if(isInsuranceEnabled & !isSalaryClient){
            creditRatio -= (ratioOfInsuranceEnabled);
            loanOfferDTO = calculationMonthlyPaymentAmount(3L ,LNR_DTO.getAmount(), LNR_DTO.getTerm(), creditRatio, isInsuranceEnabled, isSalaryClient);
        }
        else {
            loanOfferDTO = calculationMonthlyPaymentAmount(4L ,LNR_DTO.getAmount(), LNR_DTO.getTerm(), creditRatio, isInsuranceEnabled, isSalaryClient);
        }


        return loanOfferDTO;
    }


    public LoanOfferDTO calculationMonthlyPaymentAmount(Long applicationId, BigDecimal loanAmount, Integer termInYears,
                                                        Double creditRatio, Boolean isInsuranceEnabled, Boolean isSalaryClient) {

        log.info("[calculationMonthlyPaymentAmount] >> loanAmount: {}, term: {}, creditRatio: {}", loanAmount, termInYears, creditRatio);

        BigDecimal monthlyInterestRate = BigDecimal.valueOf(calculationMonthlyInterestRate(creditRatio));
        Integer interestPeriodsTerm = calculationInterestPeriodsTerm(termInYears);
        BigDecimal tempForCalc =  BigDecimal.valueOf(1).subtract(monthlyInterestRate.add(BigDecimal.valueOf(1)));

        BigDecimal monthlyPaymentAmount = loanAmount.multiply(monthlyInterestRate).divide(tempForCalc.pow(interestPeriodsTerm), RoundingMode.valueOf(5));

        BigDecimal totalAmountCalc = calculationTotalAmount(monthlyPaymentAmount, interestPeriodsTerm);

        LoanOfferDTO loanOfferDTO = LoanOfferDTO.builder()
                .applicationId(applicationId)
                .requestedAmount(loanAmount)
                .totalAmount(totalAmountCalc)
                .term(termInYears)
                .monthlyPayment(monthlyPaymentAmount)
                .rate(BigDecimal.valueOf(loanRate))
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .build();

        log.info("[calculationMonthlyPaymentAmount] << result: {}", monthlyPaymentAmount);

        return loanOfferDTO;
    }

    public Double calculationMonthlyInterestRate(Double customLoanRate) {
        log.info("[calculationMonthlyInterestRate] >> customLoanRate: {}", customLoanRate);

        Double monthlyInterestRate = customLoanRate / (100 * 12);

        log.info("[calculationMonthlyInterestRate] << result: {}", monthlyInterestRate);
        return monthlyInterestRate;
    }



    public Integer calculationInterestPeriodsTerm(Integer term) {
        log.info("[calculationInterestPeriodsTerm] >> term: {}, static countMonthOfYear: {}", term, countMonthOfYear);

        Integer countMonth = (countMonthOfYear * term) * -1;

        log.info("[calculationInterestPeriodsTerm] << result: {}", countMonth);

        return countMonth;
    }


    public BigDecimal calculationTotalAmount(BigDecimal monthlyPayment, Integer countMonth) {
        log.info("[calculationTotalAmount] >> monthlyPayment: {}, countMonth: {}", monthlyPayment, countMonth);

        BigDecimal totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(countMonth));

        log.info("[calculationTotalAmount] << result: {}", totalAmount);

        return totalAmount;
    }

}
