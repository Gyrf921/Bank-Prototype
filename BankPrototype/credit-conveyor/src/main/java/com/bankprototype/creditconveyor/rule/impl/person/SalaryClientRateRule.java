package com.bankprototype.creditconveyor.rule.impl.person;

import com.bankprototype.creditconveyor.rule.IRateRule;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class SalaryClientRateRule implements IRateRule {

    @Value("${salaryClientRateRule}")
    private Double salaryClientRateRule;

    @Override
    public BigDecimal getRate(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
        log.info("[SalaryClientRateRule.getRate] >> scoringDataDTO: {}, rate: {}", scoringDataDTO, rate);

        BigDecimal customRate = rate;
        if (scoringDataDTO.getIsSalaryClient()) {
            customRate = rate.subtract(BigDecimal.valueOf(salaryClientRateRule));
        }

        log.info("[SalaryClientRateRule.getRate] << result: {}", customRate);

        return customRate;
    }
}
