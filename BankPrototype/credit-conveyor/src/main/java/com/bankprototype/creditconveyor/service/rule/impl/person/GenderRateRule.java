package com.bankprototype.creditconveyor.service.rule.impl.person;

import com.bankprototype.creditconveyor.service.rule.IRateRule;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.extern.slf4j.Slf4j;

import com.bankprototype.creditconveyor.web.dto.enam_for_dto.Gender;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Slf4j
public class GenderRateRule implements IRateRule {
    @Override
    public BigDecimal getRate(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
        log.info("[getRate] >> scoringDataDTO: {}, rate: {}", scoringDataDTO, rate);

        int age = Period.between(scoringDataDTO.getBirthdate(), LocalDate.now()).getYears();
        BigDecimal customRate = null;
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

        log.info("[getRate] << result: {}", customRate);

        return customRate;
    }
}
