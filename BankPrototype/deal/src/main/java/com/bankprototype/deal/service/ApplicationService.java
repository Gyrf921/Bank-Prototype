package com.bankprototype.deal.service;

import com.bankprototype.deal.repository.dao.Application;
import com.bankprototype.deal.repository.dao.Client;
import com.bankprototype.deal.repository.dao.enumfordao.ApplicationStatus;
import com.bankprototype.deal.web.dto.LoanOfferDTO;

public interface ApplicationService {

    /**
     * Found and get application from DB dy applicationId
     * @param id - application id for DB
     * @return founded application
     */
    Application getApplicationById(Long id);


    /**
     * Create Application and save to DB
     * @param client - client for application
     * @return saved application
     */
    Application createApplication(Client client);


    /**
     * Automatic update status in application, set applied offer for application and saved changed in DB
     * @param loanOfferDTO - applied offer for application
     * @param status - new status for application
     * @return updated application
     */
    Application updateStatusHistoryForApplication(LoanOfferDTO loanOfferDTO, ApplicationStatus status);

}
