package com.bankprototype.creditconveyor.service.rule;

import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;

import java.math.BigDecimal;

public interface IRateRule {
    BigDecimal getRate(ScoringDataDTO scoringDataDTO, BigDecimal rate);
}
