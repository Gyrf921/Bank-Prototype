package com.bankprototype.deal.service.impl;

import com.bankprototype.deal.dao.enumfordao.ApplicationStatus;
import com.bankprototype.deal.exception.BadScoringInfoException;
import com.bankprototype.deal.service.metric.MeasureMonitoringService;
import com.bankprototype.deal.web.kafka.EmailMessageDTO;
import com.bankprototype.deal.web.kafka.enumforkafka.Theme;
import com.bankprototype.deal.dao.Application;
import com.bankprototype.deal.dao.Client;
import com.bankprototype.deal.service.ApplicationService;
import com.bankprototype.deal.service.ClientService;
import com.bankprototype.deal.service.CreditService;
import com.bankprototype.deal.service.DealProducer;
import com.bankprototype.deal.web.dto.*;
import com.bankprototype.deal.web.feign.CreditConveyorFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealService {

    private final CreditConveyorFeignClient feignClient;

    private final ClientService clientService;

    private final ApplicationService applicationService;

    private final CreditService creditService;

    private final DealProducer dealProducer;

    @Value("${topic-name.finish-registration}")
    private String finishRegistrationTopicName;

    @Value("${topic-name.create-documents}")
    private String createDocumentsTopicName;

    @Value("${topic-name.application-denied}")
    private String applicationDeniedTopicName;

    public List<LoanOfferDTO> createLoanApplication(LoanApplicationRequestDTO requestDTO) {
        log.info("[createLoanApplication] >> requestDTO: {}", requestDTO);

        Client client = clientService.createClient(requestDTO);

        Application application = applicationService.createApplication(client);

        log.info("[feignClient.calculatePossibleLoanOffers] >> requestDTO: {}", requestDTO);
        List<LoanOfferDTO> listLoanOffers = feignClient.calculatePossibleLoanOffers(requestDTO).getBody();
        log.info("[feignClient.calculatePossibleLoanOffers] << result is listLoanOffers: {}", listLoanOffers);

        listLoanOffers.forEach(loanOfferDTO -> loanOfferDTO.setApplicationId(application.getApplicationId()));

        log.info("[createLoanApplication] << result: {}", listLoanOffers);

        return listLoanOffers;
    }

    public boolean chooseOneOfTheOffers(LoanOfferDTO requestDTO) {
        log.info("[chooseOneOfTheOffers] >> requestDTO: {}", requestDTO);

        Application application = applicationService
                .updateApplicationSetLoanOffer(requestDTO);

        EmailMessageDTO massageDTO = dealProducer.createMessage(application.getApplicationId(), Theme.FINISH_REGISTRATION);

        dealProducer.sendMessage(massageDTO, finishRegistrationTopicName);

        log.info("[chooseOneOfTheOffers] << result: true");
        return true;
    }

    public boolean finishRegistration(Long applicationId, FinishRegistrationRequestDTO requestDTO) {
        log.info("[finishRegistration] >> requestDTO: {}", requestDTO);

        Application application = applicationService.getApplicationById(applicationId);
        Client client = clientService.updateClient(application.getClientId().getClientId(), requestDTO);
        ScoringDataDTO scoringDataDTO = creditService.createScoringDataDTO(requestDTO, client, application.getAppliedOffer());

        CreditDTO creditDTO = calculateCreditDTO(application, scoringDataDTO);

        creditService.createCredit(creditDTO, application);

        EmailMessageDTO massageDTO = dealProducer.createMessage(application.getApplicationId(), Theme.CREATE_DOCUMENTS);
        dealProducer.sendMessage(massageDTO, createDocumentsTopicName);

        log.info("[finishRegistration] << result: true");
        return true;
    }

    private CreditDTO calculateCreditDTO(Application application, ScoringDataDTO scoringDataDTO) {
        CreditDTO creditDTO;
        try {
            log.info("[feignClient.calculateFullLoanParameters] >> scoringDataDTO: {}", scoringDataDTO);
            creditDTO = feignClient.calculateFullLoanParameters(scoringDataDTO).getBody();
            applicationService.updateStatusForApplication(application.getApplicationId(), ApplicationStatus.CC_APPROVED);
            log.info("[feignClient.calculateFullLoanParameters] << result is creditDTO: {}", creditDTO);
        } catch (BadScoringInfoException exception) {
            EmailMessageDTO massageDTO = dealProducer.createMessage(application.getApplicationId(), Theme.APPLICATION_DENIED);
            dealProducer.sendMessage(massageDTO, applicationDeniedTopicName);
            applicationService.updateStatusForApplication(application.getApplicationId(), ApplicationStatus.CC_DENIED);

            log.error("[finishRegistration] Data validation error: BadScoringInfoException");
            throw new BadScoringInfoException(exception.getMessage());
        }
        return creditDTO;
    }

}
