package com.bankprototype.creditconveyor.rule.impl.work;

import com.bankprototype.creditconveyor.rule.RateRule;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class WorkPositionRateRule implements RateRule {

    @Value("${workPositionRateRuleWORKER}")
    private Double workPositionRateRuleWORKER;

    @Value("${workPositionRateRuleMIDDLEMANAGER}")
    private Double workPositionRateRuleMIDDLEMANAGER;

    @Value("${workPositionRateRuleTOPMANAGER}")
    private Double workPositionRateRuleTOPMANAGER;

    @Value("${workPositionRateRuleOWNER}")
    private Double workPositionRateRuleOWNER;

    @Override
    public BigDecimal getRate(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
        log.info("[WorkPositionRateRule.getRate] >> scoringDataDTO: {}, rate: {}", scoringDataDTO, rate);

        BigDecimal customRate = rate;

        switch (scoringDataDTO.getEmployment().getPosition()) {
            case WORKER:
                customRate = rate.subtract(BigDecimal.valueOf(workPositionRateRuleWORKER));
                break;
            case MIDDLE_MANAGER:
                customRate = rate.subtract(BigDecimal.valueOf(workPositionRateRuleMIDDLEMANAGER));
                break;
            case TOP_MANAGER:
                customRate = rate.subtract(BigDecimal.valueOf(workPositionRateRuleTOPMANAGER));
                break;
            case OWNER:
                customRate = rate.subtract(BigDecimal.valueOf(workPositionRateRuleOWNER));
                break;
        }

        log.info("[WorkPositionRateRule.getRate] << result: {}", customRate);

        return customRate;
    }
}