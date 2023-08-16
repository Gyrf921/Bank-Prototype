package com.bankprototype.deal.service.impl;

import com.bankprototype.deal.repository.dao.Application;
import com.bankprototype.deal.repository.dao.Client;
import com.bankprototype.deal.repository.dao.enumfordao.ApplicationStatus;
import com.bankprototype.deal.repository.dao.enumfordao.ChangeType;
import com.bankprototype.deal.repository.dao.jsonb.StatusHistory;
import com.bankprototype.deal.exception.ResourceNotFoundException;
import com.bankprototype.deal.mapper.StatusHistoryMapper;
import com.bankprototype.deal.repository.ApplicationRepository;
import com.bankprototype.deal.service.ApplicationService;
import com.bankprototype.deal.web.dto.ApplicationStatusHistoryDTO;
import com.bankprototype.deal.web.dto.LoanOfferDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    private final StatusHistoryMapper statusHistoryMapper;



    @Override
    public Application getApplicationById(Long id) {
        log.info("[getApplicationById] >> id: {}", id);

        Application application = applicationRepository.findById(id)
                .orElseThrow(() ->{
                    log.error("Application not found by this id :{} ", id);
                    return new ResourceNotFoundException("Application not found by this id :: " + id);
                });

        log.info("[getApplicationById] << result: {}", application);
        return application;
    }


    @Override
    public Application createApplication(Client client) {
        log.info("[createApplication] >> client: {}", client);

        ApplicationStatusHistoryDTO applicationStatusHistoryDTO = ApplicationStatusHistoryDTO.builder()
                .status(ApplicationStatus.PREAPPROVAL)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
                .build();

        List<StatusHistory> listStatus = List.of(statusHistoryMapper.applicationStatusHistoryDtoToStatusHistory(applicationStatusHistoryDTO));

        Application application = Application.builder()
                .clientId(client)
                .creditId(null)
                .status(ApplicationStatus.PREAPPROVAL.name())
                .creationDate(LocalDateTime.now())
                .appliedOffer(null)
                .signDate(null)
                .sesCode(null)
                .statusHistory(listStatus)
                .build();

        Application savedApplication = applicationRepository.save(application);

        log.info("[createApplication] << result: {}", savedApplication);

        return savedApplication;
    }

    @Override
    public Application updateStatusHistoryForApplication(LoanOfferDTO loanOfferDTO, ApplicationStatus status) {
        log.info("[updateStatusHistoryForApplication] >> loanOfferDTO: {}, status: {}", loanOfferDTO, status);

        Application application = getApplicationById(loanOfferDTO.getApplicationId());

        ApplicationStatusHistoryDTO applicationStatusForHistory = ApplicationStatusHistoryDTO.builder()
                .status(status)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
                .build();

        List<StatusHistory> listStatusHistory = application.getStatusHistory();

        listStatusHistory.add(statusHistoryMapper.applicationStatusHistoryDtoToStatusHistory(applicationStatusForHistory));

        application.setStatus(status.name());
        application.setStatusHistory(listStatusHistory);
        application.setAppliedOffer(loanOfferDTO);

        Application updatedApplication = applicationRepository.save(application);

        log.info("[updateStatusHistoryForApplication] << result: {}", updatedApplication);

        return updatedApplication;
    }

}
