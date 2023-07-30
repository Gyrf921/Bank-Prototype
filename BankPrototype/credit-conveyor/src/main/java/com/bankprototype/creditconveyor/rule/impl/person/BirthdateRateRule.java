package com.bankprototype.creditconveyor.rule.impl.person;

import com.bankprototype.creditconveyor.exception.BadScoringInfo;
import com.bankprototype.creditconveyor.rule.IRateRule;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Slf4j
@Component
public class BirthdateRateRule implements IRateRule {

    @Override
    public BigDecimal getRate(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
        log.info("[BirthdateRateRule.getRate] >> scoringDataDTO: {}, rate: {}", scoringDataDTO, rate);

        BigDecimal customRate = rate;
        int age = Period.between(scoringDataDTO.getBirthdate(), LocalDate.now()).getYears();

        if (age < 20 || age > 60) {
            log.error("[BirthdateRateRule.getRate] >> The client's age is less than 20 or more than 60 years, age is {}", age);
            throw new BadScoringInfo("The client's age is less than 20 or more than 60 years");
        }

        log.info("[BirthdateRateRule.getRate] << result: {}", customRate);

        return customRate;
    }
}
