package com.bankprototype.creditconveyor.service.impl;

import com.bankprototype.creditconveyor.service.ICreditCalculation;
import com.bankprototype.creditconveyor.service.IPrescoringCalculation;
import com.bankprototype.creditconveyor.web.dto.LoanApplicationRequestDTO;
import com.bankprototype.creditconveyor.web.dto.LoanOfferDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class PrescoringCalculationImpl implements IPrescoringCalculation {

    private final ICreditCalculation calculation;

    //TODO сделать нормальный ввод, потому что Value от spring не внедряет данные
    private static final Double loanRate = 8.0;
    private static final Double ratioOfInsuranceEnabled = 1.0;
    private static final Double ratioOfSalaryClient = 2.0;
    private static final BigDecimal costInsurance = BigDecimal.valueOf(100000);
    private static final int countMonthOfYear = 12;

    public PrescoringCalculationImpl(CreditCalculationImpl calculation) {
        this.calculation = calculation;
    }


    @Override
    public List<LoanOfferDTO> createListLoanOffer(LoanApplicationRequestDTO LNR_DTO){

        log.info("[createLoanOffer] >> LNR_DTO (loanApplicationRequestDTO): {}", LNR_DTO);
        BigDecimal creditRatio = BigDecimal.valueOf(loanRate);
        List<LoanOfferDTO> loanOfferDTOs = new LinkedList<>();



        loanOfferDTOs.add(calculationLoanOffer(1L ,LNR_DTO.getAmount(), LNR_DTO.getTerm(),
                creditRatio.subtract(BigDecimal.valueOf(ratioOfInsuranceEnabled + ratioOfSalaryClient)), false, false));

        loanOfferDTOs.add(calculationLoanOffer(2L ,LNR_DTO.getAmount().add(costInsurance), LNR_DTO.getTerm(),
                creditRatio.subtract(BigDecimal.valueOf(ratioOfInsuranceEnabled)), true, false));

        loanOfferDTOs.add(calculationLoanOffer(3L ,LNR_DTO.getAmount(), LNR_DTO.getTerm(),
                creditRatio.subtract(BigDecimal.valueOf(ratioOfSalaryClient)), false, true));

        loanOfferDTOs.add(calculationLoanOffer(4L ,LNR_DTO.getAmount().add(costInsurance), LNR_DTO.getTerm(),
                creditRatio, true, true));



        log.info("[createLoanOffer] << result: {}", loanOfferDTOs);

        return loanOfferDTOs;
    }



    private LoanOfferDTO calculationLoanOffer(Long applicationId, BigDecimal loanAmount, Integer termInYears,
                                              BigDecimal creditRatio, Boolean isInsuranceEnabled, Boolean isSalaryClient) {

        log.info("[calculationMonthlyPaymentAmount] >> applicationId: {}, loanAmount: {}, termInYears: {}, creditRatio: {}, isInsuranceEnabled: {}, isSalaryClient: {}"
                ,applicationId, loanAmount, termInYears, creditRatio, isInsuranceEnabled, isSalaryClient);


        BigDecimal monthlyInterestRate = calculation.calculationMonthlyInterestRate(creditRatio);
        Integer interestPeriodsTerm = calculation.calculationInterestPeriodsTerm(termInYears);
        BigDecimal monthlyPaymentAmount = calculation.calculationMonthlyPayment(loanAmount, monthlyInterestRate, interestPeriodsTerm);
        BigDecimal totalAmountCalc = calculation.calculationTotalAmount(monthlyPaymentAmount, termInYears*countMonthOfYear);


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
