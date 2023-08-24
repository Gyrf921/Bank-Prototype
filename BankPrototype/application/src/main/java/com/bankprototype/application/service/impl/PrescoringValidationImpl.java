package com.bankprototype.application.service.impl;

import com.bankprototype.application.exception.BirthdateException;
import com.bankprototype.application.service.PrescoringValidation;
import com.bankprototype.application.web.dto.LoanApplicationRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;


@Service
@Slf4j
public class PrescoringValidationImpl implements PrescoringValidation {
    @Override
    public void checkBirthdateValid(LoanApplicationRequestDTO requestDTO) {
        log.info("[checkValidLoanApplicationRequest] >> requestDTO: {}", requestDTO);

        int age = Period.between(requestDTO.getBirthdate(), LocalDate.now()).getYears();

        if (age < 18) {
            log.error("[checkValidLoanApplicationRequest.getRate] >> The client's age is less than 18, age is {}", age);
            throw new BirthdateException("The client's age is less than 18");
        }

        log.info("[checkValidLoanApplicationRequest] << result: void, birthdate is valid");
    }


}
