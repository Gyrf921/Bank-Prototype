package com.bankprototype.creditconveyor.rule;

import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;

import java.math.BigDecimal;


public interface RateRule {
    BigDecimal getRate(ScoringDataDTO scoringDataDTO, BigDecimal rate);
}
