package com.bankprototype.deal.service;

import com.bankprototype.deal.repository.dao.Client;
import com.bankprototype.deal.web.dto.FinishRegistrationRequestDTO;
import com.bankprototype.deal.web.dto.LoanApplicationRequestDTO;

public interface ClientService {

    /**
     * Found and get client from DB dy clientId
     * @param id - client id for DB
     * @return founded client
     */
    Client getClientById(Long id);

    /**
     * Create client and save to DB
     * @param requestDTO - LoanApplicationRequestDTO with some information about client
     * @return saved client
     */
    Client createClient(LoanApplicationRequestDTO requestDTO);

    /**
     * Update information about client's passport, gender, marital status, account and employment
     * @param clientId - client id for update
     * @param requestDTO - missing information about client
     * @return updated client
     */
    Client updateClient(Long clientId, FinishRegistrationRequestDTO requestDTO);
}
