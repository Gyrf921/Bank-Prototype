package com.bankprototype.creditconveyor.service.rule.impl.person;

import com.bankprototype.creditconveyor.service.rule.IRateRule;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class MaritalStatusRateRule implements IRateRule {

    @Override
    public BigDecimal getRate(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
        log.info("[getRate] >> scoringDataDTO: {}, rate: {}", scoringDataDTO, rate);

        BigDecimal customRate = null;

        switch (scoringDataDTO.getMaritalStatus())
        {
            case SINGLE:
                customRate = rate;
                break;
            case MARRIED:
                customRate = rate.subtract(BigDecimal.valueOf(3));
                break;
            case DIVORCED:
                customRate = rate.add(BigDecimal.valueOf(1));
                break;

        }

        log.info("[getRate] << result: {}", customRate);

        return customRate;
    }
}