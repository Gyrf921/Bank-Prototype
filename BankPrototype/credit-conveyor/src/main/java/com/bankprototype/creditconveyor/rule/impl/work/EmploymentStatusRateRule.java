package com.bankprototype.creditconveyor.rule.impl.work;

import com.bankprototype.creditconveyor.exception.BadScoringInfo;
import com.bankprototype.creditconveyor.rule.IRateRule;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class EmploymentStatusRateRule implements IRateRule {

    @Value("${employmentStatusRateRule_BUSINESS_OWNER}")
    private Double employmentStatusRateRule_BUSINESS_OWNER;

    @Value("${employmentStatusRateRule_SELF_EMPLOYED}")
    private Double employmentStatusRateRule_SELF_EMPLOYED;

    @Override
    public BigDecimal getRate(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
        log.info("[EmploymentStatusRateRule.getRate] >> scoringDataDTO: {}, rate: {}", scoringDataDTO, rate);

        BigDecimal customRate = rate;

        switch (scoringDataDTO.getEmployment().getEmploymentStatus())
        {
            case BUSINESS_OWNER:
                customRate = rate.add(BigDecimal.valueOf(employmentStatusRateRule_BUSINESS_OWNER));
                break;
            case SELF_EMPLOYED:
                customRate = rate.add(BigDecimal.valueOf(employmentStatusRateRule_SELF_EMPLOYED));
                break;
            case UNEMPLOYED:
                log.error("[EmploymentStatusRateRule.getRate] >> The user has EmploymentStatus: UNEMPLOYED");
                throw new BadScoringInfo("The user is unemployed");
        }

        log.info("[EmploymentStatusRateRule.getRate] << result: {}", customRate);

        return customRate;
    }
}