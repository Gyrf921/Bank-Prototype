package com.bankprototype.creditconveyor.rule.impl.person;

import com.bankprototype.creditconveyor.rule.RateRule;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Slf4j
@Component
public class GenderRateRule implements RateRule {


    @Value("${genderRateRuleMale}")
    private Double genderRateRuleMale;

    @Value("${genderRateRuleFEMALE}")
    private Double genderRateRuleFEMALE;

    @Value("${genderRateRuleNOTBINARY}")
    private Double genderRateRuleNOTBINARY;

    @Override
    public BigDecimal getRate(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
        log.info("[GenderRateRule.getRate] >> scoringDataDTO: {}, rate: {}", scoringDataDTO, rate);

        int age = Period.between(scoringDataDTO.getBirthdate(), LocalDate.now()).getYears();
        BigDecimal customRate = rate;
        switch (scoringDataDTO.getGender())
        {
            case MALE:
                if (age >= 30 && age <= 55)
                {
                    customRate = rate.subtract(BigDecimal.valueOf(genderRateRuleMale));
                }
                break;
            case FEMALE:
                if (age >= 35 && age <= 60)
                {
                    customRate = rate.subtract(BigDecimal.valueOf(genderRateRuleFEMALE));
                }
                break;
            case NOT_BINARY:
                customRate = rate.add(BigDecimal.valueOf(genderRateRuleNOTBINARY));
                break;

        }

        log.info("[GenderRateRule.getRate] << result: {}", customRate);

        return customRate;
    }
}
