package com.bankprototype.creditconveyor.rule.impl.work;

import com.bankprototype.creditconveyor.exception.BadScoringInfo;
import com.bankprototype.creditconveyor.rule.IRateRule;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class TotalWorkExperienceRateRule implements IRateRule {
    @Override
    public BigDecimal getRate(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
        log.info("[TotalWorkExperienceRateRule.getRate] >> scoringDataDTO: {}, rate: {}", scoringDataDTO, rate);

        BigDecimal customRate = rate;

        if (scoringDataDTO.getEmployment().getWorkExperienceTotal() < 12) {
            log.error("[TotalWorkExperienceRateRule.getRate] >> The total user experience is less than 3 months, total user experience is {}", scoringDataDTO.getEmployment().getWorkExperienceTotal());
            throw new BadScoringInfo("The total user experience is less than 12 months");
        }

        log.info("[TotalWorkExperienceRateRule.getRate] << result: {}", customRate);

        return customRate;

    }
}
