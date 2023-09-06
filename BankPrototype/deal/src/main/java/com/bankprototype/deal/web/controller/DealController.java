package com.bankprototype.deal.web.controller;

import com.bankprototype.deal.exception.ResourceNotFoundException;
import com.bankprototype.deal.kafka.EmailMessageDTO;
import com.bankprototype.deal.kafka.enumfordto.Theme;
import com.bankprototype.deal.repository.dao.Application;
import com.bankprototype.deal.repository.dao.Client;
import com.bankprototype.deal.repository.dao.Credit;
import com.bankprototype.deal.repository.dao.enumfordao.ApplicationStatus;
import com.bankprototype.deal.service.ApplicationService;
import com.bankprototype.deal.service.ClientService;
import com.bankprototype.deal.service.CreditService;
import com.bankprototype.deal.service.DealProducer;
import com.bankprototype.deal.web.dto.*;
import com.bankprototype.deal.web.feign.CreditConveyorFeignClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealController {

    private final CreditConveyorFeignClient feignClient;

    private final ClientService clientService;

    private final ApplicationService applicationService;

    private final CreditService creditService;

    private final DealProducer dealProducer;

    @Value("${topic-name.finish-registration}")
    private String finishRegistrationTopicName;

    @Value("${topic-name.create-documents}")
    private String createDocumentsTopicName;


    @Operation(summary = "Calculate 4 loan offers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client and application was created. Loan Offers have been calculated"),
            @ApiResponse(responseCode = "400", description = "Validation failed for some argument. Invalid input supplied"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping("/application")
    public ResponseEntity<List<LoanOfferDTO>> calculatePossibleLoanOffers(@Valid @RequestBody LoanApplicationRequestDTO requestDTO) {
        log.info("[calculatePossibleLoanOffers] >> requestDTO: {}", requestDTO);

        Client client = clientService.createClient(requestDTO);

        Application application = applicationService.createApplication(client);

        List<LoanOfferDTO> listLoanOffers = feignClient.calculatePossibleLoanOffers(requestDTO).getBody();

        listLoanOffers.forEach(loanOfferDTO -> loanOfferDTO.setApplicationId(application.getApplicationId()));

        log.info("[calculatePossibleLoanOffers] << result: {}", listLoanOffers);

        return ResponseEntity.ok().body(listLoanOffers);
    }

    @Operation(summary = "Choose one of the loan offers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status history for application has been updated"),
            @ApiResponse(responseCode = "404", description = "Not found application", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = ResourceNotFoundException.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PutMapping("/offer")
    public void chooseOneOfTheOffers(@RequestBody LoanOfferDTO loanOfferDTO) {
        log.info("[chooseOneOfTheOffers] >> loanOfferDTO: {}", loanOfferDTO);

        Application application = applicationService
                .updateStatusHistoryForApplication(loanOfferDTO, ApplicationStatus.PREAPPROVAL);

        EmailMessageDTO massageDTO = dealProducer.createMessage(application.getApplicationId(), Theme.FINISH_REGISTRATION);

        dealProducer.sendMessage(massageDTO, finishRegistrationTopicName);

        log.info("[chooseOneOfTheOffers] << result: {}", application);
    }


    @Operation(summary = "Completion registration", description = "Completion of registration and calculation of full loan terms")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status history for application has been updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed for some argument. Invalid input supplied"),
            @ApiResponse(responseCode = "404", description = "Not found some resource in database"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PutMapping("/calculate/{applicationId}")
    public void completionRegistrationAndCalculateFullCredit(@PathVariable(value = "applicationId") Long applicationId,
                                                             @Valid @RequestBody FinishRegistrationRequestDTO requestDTO) {
        log.info("[completionRegistrationAndCalculateFullCredit] >> applicationId:{}, requestDTO: {}", applicationId, requestDTO);

        Application application = applicationService.getApplicationById(applicationId);

        Client client = clientService.updateClient(application.getClientId().getClientId(), requestDTO);

        ScoringDataDTO scoringDataDTO = creditService.createScoringDataDTO(requestDTO, client, application.getAppliedOffer());

        CreditDTO creditDTO = feignClient.calculateFullLoanParameters(scoringDataDTO).getBody();

        Credit credit = creditService.createCredit(creditDTO, application);

        EmailMessageDTO massageDTO = dealProducer.createMessage(application.getApplicationId(), Theme.CREATE_DOCUMENTS);

        dealProducer.sendMessage(massageDTO, createDocumentsTopicName);

        log.info("[completionRegistrationAndCalculateFullCredit] << result: {}", credit);
    }


}
