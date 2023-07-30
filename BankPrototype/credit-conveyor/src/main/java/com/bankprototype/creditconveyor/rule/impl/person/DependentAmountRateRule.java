package com.bankprototype.creditconveyor.rule.impl.person;

import com.bankprototype.creditconveyor.rule.IRateRule;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class DependentAmountRateRule implements IRateRule {

    @Value("${dependentAmountRateRule}")
    private Double dependentAmountRateRule;

    @Override
    public BigDecimal getRate(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
        log.info("[DependentAmountRateRule.getRate] >> scoringDataDTO: {}, rate: {}", scoringDataDTO, rate);

        BigDecimal customRate = rate;
        if (scoringDataDTO.getDependentAmount() > 1) {
            customRate = rate.add(BigDecimal.valueOf(dependentAmountRateRule));
        }

        log.info("[DependentAmountRateRule.getRate] << result: {}", customRate);

        return customRate;
    }
}
