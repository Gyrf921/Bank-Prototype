package com.bankprototype.creditconveyor.service.impl;

import com.bankprototype.creditconveyor.service.IPrescoringCalculation;
import com.bankprototype.creditconveyor.web.dto.LoanApplicationRequestDTO;
import com.bankprototype.creditconveyor.web.dto.LoanOfferDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class PrescoringCalculationImpl implements IPrescoringCalculation {

    //TODO сделать нормальный ввод, потому что Value от spring не внедряет данные
    private static Double loanRate = 8.0;

    private static Double ratioOfInsuranceEnabled = 1.0;

    //@Value("${credit-conveyor.service.ratio-of-salary-client}")
    private static Double ratioOfSalaryClient = 2.0;

    private static final int countMonthOfYear = 12;

    @Override
    public List<LoanOfferDTO> createListLoanOffer(LoanApplicationRequestDTO LNR_DTO){

        log.info("[createLoanOffer] >> LNR_DTO (loanApplicationRequestDTO): {}", LNR_DTO);

        List<LoanOfferDTO> loanOfferDTOs = new LinkedList<>();

        loanOfferDTOs.add(createLoanOffer(LNR_DTO, false, false));

        loanOfferDTOs.add(createLoanOffer(LNR_DTO, true, false));

        loanOfferDTOs.add(createLoanOffer(LNR_DTO, false, true));

        loanOfferDTOs.add(createLoanOffer(LNR_DTO, true, true));

        log.info("[createLoanOffer] << result: {}", loanOfferDTOs);

        return loanOfferDTOs;
    }

    @Override
    public LoanOfferDTO createLoanOffer(LoanApplicationRequestDTO LNR_DTO, Boolean isInsuranceEnabled, Boolean isSalaryClient){

        log.info("[calculationLoanOffer] >> LNR_DTO (loanApplicationRequestDTO): {}, isInsuranceEnabled: {}, isSalaryClient: {}", LNR_DTO, isInsuranceEnabled, isSalaryClient);

        LoanOfferDTO loanOfferDTO;
        Double creditRatio = loanRate;

        if (isInsuranceEnabled & isSalaryClient){
            creditRatio -= (ratioOfInsuranceEnabled + ratioOfSalaryClient);
            loanOfferDTO = calculationLoanOffer(4L ,LNR_DTO.getAmount(), LNR_DTO.getTerm(), creditRatio, isInsuranceEnabled, isSalaryClient);
        }
        else if(!isInsuranceEnabled & isSalaryClient){
            creditRatio -= (ratioOfSalaryClient);
            loanOfferDTO = calculationLoanOffer(3L ,LNR_DTO.getAmount(), LNR_DTO.getTerm(), creditRatio, isInsuranceEnabled, isSalaryClient);
        }
        else if(isInsuranceEnabled & !isSalaryClient){
            creditRatio -= (ratioOfInsuranceEnabled);
            loanOfferDTO = calculationLoanOffer(2L ,LNR_DTO.getAmount(), LNR_DTO.getTerm(), creditRatio, isInsuranceEnabled, isSalaryClient);
        }
        else {
            loanOfferDTO = calculationLoanOffer(1L ,LNR_DTO.getAmount(), LNR_DTO.getTerm(), creditRatio, isInsuranceEnabled, isSalaryClient);
        }

        log.info("[calculationLoanOffer] << result: {}", loanOfferDTO);

        return loanOfferDTO;
    }


    private LoanOfferDTO calculationLoanOffer(Long applicationId, BigDecimal loanAmount, Integer termInYears,
                                                        Double creditRatio, Boolean isInsuranceEnabled, Boolean isSalaryClient) {

        log.info("[calculationMonthlyPaymentAmount] >> applicationId: {}, loanAmount: {}, termInYears: {}, creditRatio: {}, isInsuranceEnabled: {}, isSalaryClient: {}"
                ,applicationId, loanAmount, termInYears, creditRatio, isInsuranceEnabled, isSalaryClient);

        BigDecimal monthlyInterestRate = BigDecimal.valueOf(calculationMonthlyInterestRate(creditRatio));
        Integer interestPeriodsTerm = calculationInterestPeriodsTerm(termInYears);

        BigDecimal monthlyPaymentAmount = calculationMonthlyPaymentAmount(loanAmount, monthlyInterestRate, interestPeriodsTerm);
        BigDecimal totalAmountCalc = calculationTotalAmount(monthlyPaymentAmount, Math.abs(interestPeriodsTerm));

        LoanOfferDTO loanOfferDTO = LoanOfferDTO.builder()
                .applicationId(applicationId)
                .requestedAmount(loanAmount)
                .totalAmount(totalAmountCalc.setScale(0, RoundingMode.HALF_UP))
                .term(termInYears)
                .monthlyPayment(monthlyPaymentAmount.setScale(0, RoundingMode.HALF_UP))
                .rate(BigDecimal.valueOf(creditRatio))
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .build();

        log.info("[calculationMonthlyPaymentAmount] << result: {}", loanOfferDTO);

        return loanOfferDTO;
    }

    private BigDecimal calculationMonthlyPaymentAmount(BigDecimal loanAmount, BigDecimal monthlyInterestRate, Integer interestPeriodsTerm) {
        log.info("[calculationMonthlyPaymentAmount] >> loanAmount: {}, monthlyInterestRate: {}, interestPeriodsTerm: {}", loanAmount,monthlyInterestRate,interestPeriodsTerm);

        BigDecimal tempSumForPow = BigDecimal.ONE.add(monthlyInterestRate);
        BigDecimal temp = tempSumForPow.pow(Math.abs(interestPeriodsTerm));
        BigDecimal tempPow = BigDecimal.ONE.divide(temp, temp.scale(), RoundingMode.DOWN);
        BigDecimal tempForCalc = BigDecimal.ONE.subtract(tempPow);

        BigDecimal monthlyPaymentAmount = loanAmount.multiply(monthlyInterestRate).divide(tempForCalc, RoundingMode.valueOf(5));

        log.info("[calculationMonthlyPaymentAmount] << result: {}", monthlyPaymentAmount);

        return monthlyPaymentAmount;
    }

    private Double calculationMonthlyInterestRate(Double customLoanRate) {
        log.info("[calculationMonthlyInterestRate] >> customLoanRate: {}", customLoanRate);

        Double monthlyInterestRate = customLoanRate / (100 * 12);

        log.info("[calculationMonthlyInterestRate] << result: {}", monthlyInterestRate);

        return monthlyInterestRate;
    }


    private Integer calculationInterestPeriodsTerm(Integer term) {
        log.info("[calculationInterestPeriodsTerm] >> term: {}, static countMonthOfYear: {}", term, countMonthOfYear);

        Integer countMonth = (countMonthOfYear * term) * -1;

        log.info("[calculationInterestPeriodsTerm] << result: {}", countMonth);

        return countMonth;
    }


    private BigDecimal calculationTotalAmount(BigDecimal monthlyPayment, Integer countMonth) {
        log.info("[calculationTotalAmount] >> monthlyPayment: {}, countMonth: {}", monthlyPayment, countMonth);

        BigDecimal totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(countMonth));

        log.info("[calculationTotalAmount] << result: {}", totalAmount);

        return totalAmount;
    }

}
