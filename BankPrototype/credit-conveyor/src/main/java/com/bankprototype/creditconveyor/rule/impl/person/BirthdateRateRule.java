package com.bankprototype.creditconveyor.rule.impl.person;

import com.bankprototype.creditconveyor.exception.BadScoringInfoException;
import com.bankprototype.creditconveyor.rule.RateRule;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Slf4j
@Component
public class BirthdateRateRule implements RateRule {

    @Override
    public BigDecimal getRate(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
        log.info("[BirthdateRateRule.getRate] >> scoringDataDTO: {}, rate: {}", scoringDataDTO, rate);

        int age = Period.between(scoringDataDTO.getBirthDate(), LocalDate.now()).getYears();

        if (age < 20 || age > 60) {
            log.error("[BirthdateRateRule.getRate] >> The client's age is less than 20 or more than 60 years, age is {}", age);
            throw new BadScoringInfoException("The client's age is less than 20 or more than 60 years");
        }

        log.info("[BirthdateRateRule.getRate] << result: {}", rate);

        return rate;
    }
}
