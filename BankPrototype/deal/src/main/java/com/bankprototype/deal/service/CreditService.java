package com.bankprototype.deal.service;

import com.bankprototype.deal.repository.dao.Application;
import com.bankprototype.deal.repository.dao.Client;
import com.bankprototype.deal.repository.dao.Credit;
import com.bankprototype.deal.web.dto.CreditDTO;
import com.bankprototype.deal.web.dto.FinishRegistrationRequestDTO;
import com.bankprototype.deal.web.dto.LoanOfferDTO;
import com.bankprototype.deal.web.dto.ScoringDataDTO;

public interface CreditService {

    /**
     * Create ScoringDataDTO to calculate the terms of the loan
     *
     * @param requestDTO   - important information about clients
     * @param client       - another information about client
     * @param loanOfferDTO - information about terms of the loan
     * @return scoring Data
     */
    ScoringDataDTO createScoringDataDTO(FinishRegistrationRequestDTO requestDTO, Client client, LoanOfferDTO loanOfferDTO);

    /**
     * Create  new credit and save it to DB
     *
     * @param creditDTO   - information about credits terms
     * @param application - information about application for update
     * @return saved Credit
     */
    Credit createCredit(CreditDTO creditDTO, Application application);

}
