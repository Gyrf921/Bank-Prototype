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
public class TotalWorkExperienceRateRule implements RateRule {

    @Value("${minimalTotalWorkExperience}")
    private Integer minimalTotalWorkExperience;

    @Override
    public BigDecimal getRate(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
        log.info("[TotalWorkExperienceRateRule.getRate] >> scoringDataDTO: {}, rate: {}", scoringDataDTO, rate);

        if (scoringDataDTO.getEmployment().getWorkExperienceTotal() < minimalTotalWorkExperience) {
            log.error("[TotalWorkExperienceRateRule.getRate] >> The total user experience is less than 3 months, total user experience is {}", scoringDataDTO.getEmployment().getWorkExperienceTotal());
            throw new BadScoringInfoException("The total user experience is less than 12 months");
        }

        log.info("[TotalWorkExperienceRateRule.getRate] << result: {}", rate);

        return rate;

    }
}
