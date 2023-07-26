package com.bankprototype.creditconveyor.service.rule.impl.work;

import com.bankprototype.creditconveyor.service.rule.IRateRule;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.extern.slf4j.Slf4j;

import com.bankprototype.creditconveyor.web.dto.enam_for_dto.Position;
import java.math.BigDecimal;

@Slf4j
public class WorkPositionRateRule implements IRateRule {
    @Override
    public BigDecimal getRate(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
        log.info("[getRate] >> scoringDataDTO: {}, rate: {}", scoringDataDTO, rate);

        BigDecimal customRate = null;

        switch (scoringDataDTO.getEmployment().getPosition())
        {
            case MIDDLE_MANAGER:
                customRate = rate.subtract(BigDecimal.valueOf(2));
                break;
            case TOP_MANAGER:
                customRate = rate.subtract(BigDecimal.valueOf(4));
                break;
        }

        log.info("[getRate] << result: {}", customRate);

        return customRate;
    }
}