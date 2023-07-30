package com.bankprototype.creditconveyor.rule.impl.person;

import com.bankprototype.creditconveyor.rule.IRateRule;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class MaritalStatusRateRule implements IRateRule {

    @Value("${maritalStatusRateRule_MARRIED}")
    private Double maritalStatusRateRule_MARRIED;

    @Value("${maritalStatusRateRule_DIVORCED}")
    private Double maritalStatusRateRule_DIVORCED;

    @Override
    public BigDecimal getRate(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
        log.info("[MaritalStatusRateRule.getRate] >> scoringDataDTO: {}, rate: {}", scoringDataDTO, rate);

        BigDecimal customRate = null;

        switch (scoringDataDTO.getMaritalStatus())
        {
            case SINGLE:
                customRate = rate;
                break;
            case MARRIED:
                customRate = rate.subtract(BigDecimal.valueOf(maritalStatusRateRule_MARRIED));
                break;
            case DIVORCED:
                customRate = rate.add(BigDecimal.valueOf(maritalStatusRateRule_DIVORCED));
                break;

        }

        log.info("[MaritalStatusRateRule.getRate] << result: {}", customRate);

        return customRate;
    }
}