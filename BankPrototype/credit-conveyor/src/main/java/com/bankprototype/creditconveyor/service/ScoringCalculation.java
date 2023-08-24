package com.bankprototype.creditconveyor.service;

import com.bankprototype.creditconveyor.web.dto.CreditDTO;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;

public interface ScoringCalculation {

    /**
     * Создание одного предложения по кредиту со всей вводной информацией
     *
     * @param scoringDataDTO - Вводные данные для офрмирования кредита
     */
    CreditDTO createCredit(ScoringDataDTO scoringDataDTO);
}
