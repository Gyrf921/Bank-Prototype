package com.bankprototype.creditconveyor.rule.impl.work;

import com.bankprototype.creditconveyor.exception.BadScoringInfo;
import com.bankprototype.creditconveyor.rule.IRateRule;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class CurrentWorkExperienceRateRule implements IRateRule {
    @Override
    public BigDecimal getRate(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
        log.info("[CurrentWorkExperienceRateRule.getRate] >> scoringDataDTO: {}, rate: {}", scoringDataDTO, rate);

        if (scoringDataDTO.getEmployment().getWorkExperienceCurrent() < 3) {
            log.error("[CurrentWorkExperienceRateRule.getRate] >> The current user experience is less than 3 months, Current Work Experience is {}", scoringDataDTO.getEmployment().getWorkExperienceCurrent());
            throw new BadScoringInfo("The current user experience is less than 3 months");
        }
        log.info("[CurrentWorkExperienceRateRule.getRate] << result: {}", rate);

        return rate;
    }
}
