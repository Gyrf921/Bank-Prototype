package com.bankprototype.creditconveyor.rule.impl.work;

import com.bankprototype.creditconveyor.exception.BadScoringInfo;
import com.bankprototype.creditconveyor.rule.IRateRule;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class SalaryRateRule  implements IRateRule {
    @Override
    public BigDecimal getRate(ScoringDataDTO scoringDataDTO, BigDecimal rate) {
        log.info("[SalaryRateRule.getRate] >> scoringDataDTO: {}, rate: {}", scoringDataDTO, rate);

        BigDecimal twentySalary = scoringDataDTO.getEmployment().getSalary().multiply(BigDecimal.valueOf(20));

        if (twentySalary.compareTo(scoringDataDTO.getAmount()) < 0) {
            log.error("[SalaryRateRule.getRate] >> The user asks for an amount of 20 or more in excess of his salary, 20 salaries are {}, user asks {}, which is {} more than the allowed amount", twentySalary, scoringDataDTO.getAmount(), scoringDataDTO.getAmount().subtract(twentySalary));
            throw new BadScoringInfo("The user asks for an amount of 20 or more in excess of his salary");
        }

        log.info("[SalaryRateRule.getRate] << result: {}", rate);

        return rate;
    }
}