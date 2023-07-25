package com.bankprototype.creditconveyor.service;

import com.bankprototype.creditconveyor.web.dto.LoanApplicationRequestDTO;
import com.bankprototype.creditconveyor.web.dto.LoanOfferDTO;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;

public interface IScoringCalculation {

    /**
     * Создание одного предложения по кредиту со всей вводной информацией
     * @param scoringDataDTO - Вводные данные для офрмирования кредита
     */
    LoanOfferDTO createCredit(ScoringDataDTO scoringDataDTO);
}
