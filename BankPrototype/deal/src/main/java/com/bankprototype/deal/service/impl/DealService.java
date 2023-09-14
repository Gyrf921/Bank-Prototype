package com.bankprototype.deal.service.impl;

import com.bankprototype.deal.exception.BadScoringInfoException;
import com.bankprototype.deal.exception.ExternalException;
import com.bankprototype.deal.exception.global.ErrorDetails;
import com.bankprototype.deal.kafka.EmailMessageDTO;
import com.bankprototype.deal.kafka.enumfordto.Theme;
import com.bankprototype.deal.repository.dao.Application;
import com.bankprototype.deal.repository.dao.Client;
import com.bankprototype.deal.repository.dao.enumfordao.ApplicationStatus;
import com.bankprototype.deal.service.ApplicationService;
import com.bankprototype.deal.service.ClientService;
import com.bankprototype.deal.service.CreditService;
import com.bankprototype.deal.service.DealProducer;
import com.bankprototype.deal.web.dto.*;
import com.bankprototype.deal.web.feign.CreditConveyorFeignClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.ByteBuffer;
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
                .updateStatusHistoryForApplication(requestDTO, ApplicationStatus.PREAPPROVAL);

        EmailMessageDTO massageDTO = dealProducer.createMessage(application.getApplicationId(), Theme.FINISH_REGISTRATION);

        dealProducer.sendMessage(massageDTO, finishRegistrationTopicName);

        log.info("[chooseOneOfTheOffers] << result: true");
        return true;
    }

    public boolean finishRegistration(Long applicationId, FinishRegistrationRequestDTO requestDTO) {
        log.info("[chooseOneOfTheOffers] >> requestDTO: {}", requestDTO);

        Application application = applicationService.getApplicationById(applicationId);

        Client client = clientService.updateClient(application.getClientId().getClientId(), requestDTO);

        ScoringDataDTO scoringDataDTO = creditService.createScoringDataDTO(requestDTO, client, application.getAppliedOffer());
        CreditDTO creditDTO = null;
        try{
            log.info("[feignClient.calculateFullLoanParameters] >> scoringDataDTO: {}", scoringDataDTO);
            creditDTO = feignClient.calculateFullLoanParameters(scoringDataDTO).getBody();
            log.info("[feignClient.calculateFullLoanParameters] << result is creditDTO: {}", creditDTO);
        }
        catch (BadScoringInfoException exception){
            EmailMessageDTO massageDTO = dealProducer.createMessage(application.getApplicationId(), Theme.APPLICATION_DENIED);
            dealProducer.sendMessage(massageDTO, applicationDeniedTopicName);
            //exception.getErrorDetails().getStatusCode(), exception.getErrorDetails(),
            throw new BadScoringInfoException(exception.getErrorDetails().getMessage());
        }

        creditService.createCredit(creditDTO, application);

        EmailMessageDTO massageDTO = dealProducer.createMessage(application.getApplicationId(), Theme.CREATE_DOCUMENTS);
        dealProducer.sendMessage(massageDTO, createDocumentsTopicName);

        log.info("[chooseOneOfTheOffers] << result: true");
        return true;
    }



}
