package com.bankprototype.creditconveyor.service.rule;

import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Data
@AllArgsConstructor
public class RateRuleEngine implements IRateRule {

    List<IRateRule> rules;

    @Override
    public BigDecimal getRate(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
        log.info("[getRate] >> scoringDataDTO: {}, rate: {}", scoringDataDTO,rate);

        BigDecimal customRate = rate;

        for(IRateRule rule : rules)
        {
            customRate = rule.getRate(scoringDataDTO, customRate);

            if (customRate.equals(BigDecimal.ZERO) || customRate.compareTo(BigDecimal.ZERO) < 0){

                log.info("[getRate] << return: {}", BigDecimal.ZERO);
                return BigDecimal.ZERO;
            }
        }

        log.info("[getRate] << return: {}", customRate);

        return customRate;
    }
}
