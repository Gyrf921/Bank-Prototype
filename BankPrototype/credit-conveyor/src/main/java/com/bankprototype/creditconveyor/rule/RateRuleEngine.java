package com.bankprototype.creditconveyor.rule;

import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Data
@Component
public class RateRuleEngine implements IRateRule {

    private List<IRateRule> rateRules;

    @Autowired
    public RateRuleEngine(List<IRateRule> rateRules) {
        this.rateRules = rateRules;
    }

    @Override
    public BigDecimal getRate(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
        log.info("[getRate] >> scoringDataDTO: {}, rate: {}", scoringDataDTO,rate);

        BigDecimal customRate = rate;

        for(IRateRule rule : rateRules)
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
