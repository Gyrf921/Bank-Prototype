package com.bankprototype.creditconveyor.service.rule.impl.person;

import com.bankprototype.creditconveyor.service.rule.IRateRule;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class DependentAmountRateRule implements IRateRule {
    @Override
    public BigDecimal getRate(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
        log.info("[getRate] >> scoringDataDTO: {}, rate: {}", scoringDataDTO, rate);

        BigDecimal customRate = null;
        if (scoringDataDTO.getDependentAmount() > 1) {
            customRate = rate.add(BigDecimal.valueOf(1)); //TODO эти значения нужно брать из ресурсов
        }
        else {
            customRate = rate;
        }

        log.info("[getRate] << result: {}", customRate);

        return customRate;
    }
}
