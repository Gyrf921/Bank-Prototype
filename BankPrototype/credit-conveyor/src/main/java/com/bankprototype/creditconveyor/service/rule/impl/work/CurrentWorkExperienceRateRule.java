package com.bankprototype.creditconveyor.service.rule.impl.work;

import com.bankprototype.creditconveyor.service.rule.IRateRule;
import com.bankprototype.creditconveyor.web.dto.EmploymentDTO;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Slf4j
public class CurrentWorkExperienceRateRule implements IRateRule {
    @Override
    public BigDecimal getRate(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
        log.info("[getRate] >> scoringDataDTO: {}, rate: {}", scoringDataDTO, rate);

        BigDecimal customRate = null;

        if (scoringDataDTO.getEmployment().getWorkExperienceCurrent() < 3)
            customRate = BigDecimal.valueOf(1000000000);
        else
            customRate = rate;

        log.info("[getRate] << result: {}", customRate);

        return customRate;
    }
}
