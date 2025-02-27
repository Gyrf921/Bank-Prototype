package com.bankprototype.deal.service.impl;

import com.bankprototype.deal.exception.ResourceNotFoundException;
import com.bankprototype.deal.dao.mapper.StatusHistoryMapper;
import com.bankprototype.deal.repository.ApplicationRepository;
import com.bankprototype.deal.dao.Application;
import com.bankprototype.deal.dao.Client;
import com.bankprototype.deal.dao.enumfordao.ApplicationStatus;
import com.bankprototype.deal.dao.jsonb.StatusHistory;
import com.bankprototype.deal.service.ApplicationService;
import com.bankprototype.deal.service.metric.MeasureMonitoringService;
import com.bankprototype.deal.web.dto.ApplicationStatusHistoryDTO;
import com.bankprototype.deal.web.dto.LoanOfferDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    private final StatusHistoryMapper statusHistoryMapper;

    private final MeasureMonitoringService measureMonitoringService;

    @Override
    public Application getApplicationById(Long id) {
        log.info("[getApplicationById] >> id: {}", id);

        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Application not found by this id :{} ", id);
                    return new ResourceNotFoundException("Application not found by this id :: " + id);
                });

        log.info("[getApplicationById] << result: {}", application);
        return application;
    }

    @Override
    public List<Application> getAllApplication() {
        log.info("[getAllApplication] >> without resources");

        List<Application> applications = applicationRepository.findAll();

        log.info("[getAllApplication] << result: {}", applications);

        return applications;
    }

    @Override
    public Application createApplication(Client client) {
        log.info("[createApplication] >> client: {}", client);

        Application application = Application.builder()
                .clientId(client)
                .creationDate(LocalDateTime.now())
                .build();

        setApplicationStatus(application, ApplicationStatus.PREAPPROVAL);

        Application savedApplication = applicationRepository.save(application);

        log.info("[createApplication] << result: {}", savedApplication);

        return savedApplication;
    }

    @Override
    public Application updateStatusForApplication(Long applicationId, ApplicationStatus status){
        log.info("[updateStatusForApplication] >> applicationId: {}, status: {}", applicationId, status);
        Application application = getApplicationById(applicationId);

        setApplicationStatus(application, status);

        Application updatedApplication = applicationRepository.save(application);
        log.info("[updateStatusForApplication] >> return: {}", updatedApplication);

        return updatedApplication;
    }

    private void setApplicationStatus(Application application, ApplicationStatus newStatus){
        log.info("[changeApplicationStatus] >> application: {}, newStatus: {}", application, newStatus);
        ApplicationStatusHistoryDTO applicationStatusForHistory = ApplicationStatusHistoryDTO.createAuto(newStatus);

        List<StatusHistory> listStatusHistory = application.getStatusHistory();
        if (listStatusHistory == null) {
            listStatusHistory = new ArrayList<>();
        }
        listStatusHistory.add(statusHistoryMapper.map(applicationStatusForHistory));

        application.setStatus(newStatus);
        application.setStatusHistory(listStatusHistory);

        measureMonitoringService.incrementStatusCounter(application.getStatus());
    }

    @Override
    public Application updateApplicationSetLoanOffer(LoanOfferDTO loanOfferDTO) {
        log.info("[setLoanOfferForApplication] >> loanOfferDTO: {}", loanOfferDTO);

        Application application = getApplicationById(loanOfferDTO.getApplicationId());

        application.setAppliedOffer(loanOfferDTO);
        setApplicationStatus(application, ApplicationStatus.APPROVED);

        Application updatedApplication = applicationRepository.save(application);

        log.info("[setLoanOfferForApplication] << result: {}", updatedApplication);

        return updatedApplication;
    }

    @Override
    public Application updateApplicationSetSignDate(Application application) {
        log.info("[updateApplicationSetSignDate] >> applicationId: {}", application.getApplicationId());

        application.setSignDate(Timestamp.valueOf(LocalDateTime.now()));

        Application updatedApplication = applicationRepository.save(application);

        log.info("[updateApplicationSetSignDate] << result: {}", updatedApplication);

        return updatedApplication;
    }

    @Override
    public Application updateSesCodeForApplication(Long applicationId) {
        log.info("[updateSesCodeForApplication] >> applicationId: {}", applicationId);

        Application application = getApplicationById(applicationId);

        application.setSesCode(generateSesCode());

        Application updatedApplication = applicationRepository.save(application);

        log.info("[updateSesCodeForApplication] << result: {}", updatedApplication);

        return updatedApplication;
    }

    private Long generateSesCode() {
        long minimum = 100000L;
        long maximum = 999999L;

        log.info("[Generated ses code] << minimum: {}, minimum: {}", minimum, maximum);
        return (long) (Math.random() * (maximum - minimum) + minimum);
    }

    @Override
    public boolean checkingCorrectnessSesCode(Long applicationId, Long sesCode) {
        log.info("[checkingCorrectnessSesCode] >> applicationId: {}, sesCode: {}", applicationId, sesCode);
        return getApplicationById(applicationId).getSesCode().equals(sesCode);
    }

}
