package com.bankprototype.creditconveyor.service;

import com.bankprototype.creditconveyor.web.dto.LoanApplicationRequestDTO;
import com.bankprototype.creditconveyor.web.dto.LoanOfferDTO;

import java.util.List;

public interface PrescoringCalculation {


    /**
     * Создание листа предложений по кредиту
     *
     * @param requestDTO - Вводные данные для получения кредита
     */
    List<LoanOfferDTO> createListLoanOffer(LoanApplicationRequestDTO requestDTO);


}

