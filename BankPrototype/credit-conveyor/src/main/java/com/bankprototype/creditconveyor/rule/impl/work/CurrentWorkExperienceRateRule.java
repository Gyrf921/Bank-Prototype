package com.bankprototype.creditconveyor.rule.impl.work;

import com.bankprototype.creditconveyor.exception.BadScoringInfoException;
import com.bankprototype.creditconveyor.rule.RateRule;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class CurrentWorkExperienceRateRule implements RateRule {

    @Value("${minimalCurrentWorkExperience}")
    private Integer minimalCurrentWorkExperience;

    @Override
    public BigDecimal getRate(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
        log.info("[CurrentWorkExperienceRateRule.getRate] >> scoringDataDTO: {}, rate: {}", scoringDataDTO, rate);

        if (scoringDataDTO.getEmployment().getWorkExperienceCurrent() < minimalCurrentWorkExperience) {
            log.error("[CurrentWorkExperienceRateRule.getRate] >> The current user experience is less than 3 months, Current Work Experience is {}", scoringDataDTO.getEmployment().getWorkExperienceCurrent());
            throw new BadScoringInfoException("The current user experience is less than 3 months");
        }
        log.info("[CurrentWorkExperienceRateRule.getRate] << result: {}", rate);

        return rate;
    }
}
