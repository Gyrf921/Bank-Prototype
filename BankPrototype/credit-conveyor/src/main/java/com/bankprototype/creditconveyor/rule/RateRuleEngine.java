package com.bankprototype.creditconveyor.rule;

import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Data
@Component
@RequiredArgsConstructor
@PropertySource("classpath:scoringConfig.properties")
public class RateRuleEngine implements RateRule {

    private final List<RateRule> rateRules;

    @Override
    public BigDecimal getRate(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
        log.info("[getRate] >> scoringDataDTO: {}, rate: {}", scoringDataDTO, rate);

        BigDecimal customRate = rate;

        for (RateRule rule : rateRules) {
            customRate = rule.getRate(scoringDataDTO, customRate);

        }

        log.info("[getRate] << return: {}", customRate);

        return customRate;
    }
}
