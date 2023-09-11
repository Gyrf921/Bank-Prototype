package com.bankprototype.deal.service;

import com.bankprototype.deal.repository.dao.Application;
import com.bankprototype.deal.repository.dao.Client;
import com.bankprototype.deal.repository.dao.enumfordao.ApplicationStatus;
import com.bankprototype.deal.web.dto.LoanOfferDTO;

import java.util.List;

public interface ApplicationService {


    /**
     * Found and get application from DB dy applicationId
     *
     * @param id - application id for DB
     * @return founded application
     */
    Application getApplicationById(Long id);


    /**
     * Found and get all applications from DB
     *
     * @return founded applications
     */
    List<Application> getAllApplication();

    /**
     * Create Application and save to DB
     *
     * @param client - client for application
     * @return saved application
     */
    Application createApplication(Client client);


    /**
     * Automatic update status in application, set applied offer for application and saved changed in DB
     *
     * @param loanOfferDTO - applied offer for application
     * @param status       - new status for application
     * @return updated application
     */
    Application updateStatusHistoryForApplication(LoanOfferDTO loanOfferDTO, ApplicationStatus status);


    /**
     * Update ses code  in application and saved changed in DB
     *
     * @param applicationId - application for ses code
     * @return updated application
     */
    Application updateSesCodeForApplication(Long applicationId);

    /**
     * Check user's ses code with code from DB
     *
     * @param applicationId - application for ses code
     * @param sesCode       - ses code for check
     * @return true if ses code is correct
     */
    boolean checkingCorrectnessSesCode(Long applicationId, Long sesCode);
}
