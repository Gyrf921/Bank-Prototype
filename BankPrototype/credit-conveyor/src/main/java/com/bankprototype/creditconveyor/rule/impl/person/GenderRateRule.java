package com.bankprototype.creditconveyor.rule.impl.person;

import com.bankprototype.creditconveyor.rule.IRateRule;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Slf4j
@Component
public class GenderRateRule implements IRateRule {


    @Value("${genderRateRule_Male}")
    private Double genderRateRule_Male;

    @Value("${genderRateRule_FEMALE}")
    private Double genderRateRule_FEMALE;

    @Value("${genderRateRule_NOT_BINARY}")
    private Double genderRateRule_NOT_BINARY;

    @Override
    public BigDecimal getRate(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
        log.info("[GenderRateRule.getRate] >> scoringDataDTO: {}, rate: {}", scoringDataDTO, rate);

        int age = Period.between(scoringDataDTO.getBirthdate(), LocalDate.now()).getYears();
        BigDecimal customRate = rate;
        switch (scoringDataDTO.getGender())
        {
            case MALE:
                if (age >= 30 & age <= 55)
                {
                    customRate = rate.subtract(BigDecimal.valueOf(3));
                }
                break;
            case FEMALE:
                if (age >= 35 & age <= 60)
                {
                    customRate = rate.subtract(BigDecimal.valueOf(3));
                }
                break;
            case NOT_BINARY:
                customRate = rate.add(BigDecimal.valueOf(3));
                break;

        }

        log.info("[GenderRateRule.getRate] << result: {}", customRate);

        return customRate;
    }
}
