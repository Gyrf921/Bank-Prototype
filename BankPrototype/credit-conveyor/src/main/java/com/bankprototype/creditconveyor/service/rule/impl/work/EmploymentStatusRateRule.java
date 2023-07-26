package com.bankprototype.creditconveyor.service.rule.impl.work;

import com.bankprototype.creditconveyor.service.rule.IRateRule;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class EmploymentStatusRateRule implements IRateRule {
    @Override
    public BigDecimal getRate(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
        log.info("[getRate] >> scoringDataDTO: {}, rate: {}", scoringDataDTO, rate);

        BigDecimal customRate = null;

        switch (scoringDataDTO.getEmployment().getEmploymentStatus())
        {
            case BUSINESS_OWNER:
                customRate = rate.add(BigDecimal.valueOf(3));
                break;
            case SELF_EMPLOYED:
                customRate = rate.add(BigDecimal.valueOf(1));
                break;
            case UNEMPLOYED:
                customRate = BigDecimal.valueOf(1000000000);
                break;
        }

        log.info("[getRate] << result: {}", customRate);

        return customRate;
    }
}