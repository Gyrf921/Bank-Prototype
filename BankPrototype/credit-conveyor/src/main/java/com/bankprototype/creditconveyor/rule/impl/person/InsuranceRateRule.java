package com.bankprototype.creditconveyor.rule.impl.person;

import com.bankprototype.creditconveyor.rule.IRateRule;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class InsuranceRateRule implements IRateRule {

    @Value("${insuranceRateRule}")
    private Double insuranceRateRule;

    @Override
    public BigDecimal getRate(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
        log.info("[InsuranceRateRule.getRate] >> scoringDataDTO: {}, rate: {}", scoringDataDTO, rate);

        BigDecimal customRate = rate;
        if (scoringDataDTO.getIsInsuranceEnabled()) {
            customRate = rate.subtract(BigDecimal.valueOf(insuranceRateRule));
        }

        log.info("[InsuranceRateRule.getRate] << result: {}", customRate);

        return customRate;
    }
}
