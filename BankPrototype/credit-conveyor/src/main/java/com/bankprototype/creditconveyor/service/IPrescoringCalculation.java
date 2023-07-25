
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


    /**
     * Создание каждого предложения по кредиту на основании условий страхования и зарплатности клиента
     * @param LNR_DTO - Вводные данные для получения кредита
     * @param isInsuranceEnabled - является ли полльзователь страхователем жизни
     * @param isSalaryClient - является ли полльхователь зарплатным клинетом
     */
    LoanOfferDTO createLoanOffer(LoanApplicationRequestDTO LNR_DTO, Boolean isInsuranceEnabled, Boolean isSalaryClient);



}

