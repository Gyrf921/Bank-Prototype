package com.bankprototype.creditconveyor.rule.impl.person;

import com.bankprototype.creditconveyor.rule.RateRule;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class MaritalStatusRateRule implements RateRule {

    @Value("${maritalStatusRateRuleSINGLE}")
    private Double maritalStatusRateRuleSINGLE;

    @Value("${maritalStatusRateRuleMARRIED}")
    private Double maritalStatusRateRuleMARRIED;

    @Value("${maritalStatusRateRuleDIVORCED}")
    private Double maritalStatusRateRuleDIVORCED;

    @Value("${maritalStatusRateRuleWIDOWWIDOWER}")
    private Double maritalStatusRateRuleWIDOWWIDOWER;

    @Override
    public BigDecimal getRate(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
        log.info("[MaritalStatusRateRule.getRate] >> scoringDataDTO: {}, rate: {}", scoringDataDTO, rate);

        BigDecimal customRate = null;

        switch (scoringDataDTO.getMaritalStatus()) {
            case SINGLE:
                customRate = rate.subtract(BigDecimal.valueOf(maritalStatusRateRuleSINGLE));
                break;
            case MARRIED:
                customRate = rate.subtract(BigDecimal.valueOf(maritalStatusRateRuleMARRIED));
                break;
            case DIVORCED:
                customRate = rate.add(BigDecimal.valueOf(maritalStatusRateRuleDIVORCED));
                break;
            case WIDOW_WIDOWER:
                customRate = rate.subtract(BigDecimal.valueOf(maritalStatusRateRuleWIDOWWIDOWER));
                break;
        }

        log.info("[MaritalStatusRateRule.getRate] << result: {}", customRate);

        return customRate;
    }
}