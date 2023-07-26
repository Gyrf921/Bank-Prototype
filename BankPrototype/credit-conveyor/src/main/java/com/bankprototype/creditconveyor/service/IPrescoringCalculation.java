
package com.bankprototype.creditconveyor.service;

import com.bankprototype.creditconveyor.web.dto.LoanApplicationRequestDTO;
import com.bankprototype.creditconveyor.web.dto.LoanOfferDTO;

import java.util.List;

public interface IPrescoringCalculation {


    /**
     * Создание листа предложений по кредиту
     * @param LNR_DTO - Вводные данные для получения кредита
     */
    List<LoanOfferDTO> createListLoanOffer(LoanApplicationRequestDTO LNR_DTO);


}

