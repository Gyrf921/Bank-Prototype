package com.bankprototype.creditconveyor.rule.impl.work;

import com.bankprototype.creditconveyor.exception.BadScoringInfoException;
import com.bankprototype.creditconveyor.rule.RateRule;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class EmploymentStatusRateRule implements RateRule {

    @Value("${employmentStatusRateRuleBUSINESSOWNER}")
    private Double employmentStatusRateRuleBUSINESSOWNER;

    @Value("${employmentStatusRateRuleSELFEMPLOYED}")
    private Double employmentStatusRateRuleSELFEMPLOYED;

    @Override
    public BigDecimal getRate(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
        log.info("[EmploymentStatusRateRule.getRate] >> scoringDataDTO: {}, rate: {}", scoringDataDTO, rate);

        BigDecimal customRate = rate;

        switch (scoringDataDTO.getEmployment().getEmploymentStatus())
        {
            case BUSINESS_OWNER:
                customRate = rate.add(BigDecimal.valueOf(employmentStatusRateRuleBUSINESSOWNER));
                break;
            case SELF_EMPLOYED:
                customRate = rate.add(BigDecimal.valueOf(employmentStatusRateRuleSELFEMPLOYED));
                break;
            case UNEMPLOYED:
                log.error("[EmploymentStatusRateRule.getRate] >> The user has EmploymentStatus: UNEMPLOYED");
                throw new BadScoringInfoException("The user is unemployed");
        }

        log.info("[EmploymentStatusRateRule.getRate] << result: {}", customRate);

        return customRate;
    }
}