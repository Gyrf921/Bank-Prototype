package com.bankprototype.deal.web.controller;

import com.bankprototype.deal.exception.ResourceNotFoundException;
import com.bankprototype.deal.repository.dao.Application;
import com.bankprototype.deal.repository.dao.Client;
import com.bankprototype.deal.repository.dao.Credit;
import com.bankprototype.deal.repository.dao.enumfordao.ApplicationStatus;
import com.bankprototype.deal.service.ApplicationService;
import com.bankprototype.deal.service.ClientService;
import com.bankprototype.deal.service.CreditService;
import com.bankprototype.deal.service.DealProducer;
import com.bankprototype.deal.web.dto.*;
import com.bankprototype.deal.web.dto.enumfordto.Theme;
import com.bankprototype.deal.web.feign.CreditConveyorFeignClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Operation(summary = "Calculate 4 loan offers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client and application was created. Loan Offers have been calculated"),
            @ApiResponse(responseCode = "400", description = "Validation failed for some argument. Invalid input supplied"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping("/application")
    public List<LoanOfferDTO> calculatePossibleLoanOffers(@Valid @RequestBody LoanApplicationRequestDTO requestDTO) {
        log.info("[calculatePossibleLoanOffers] >> requestDTO: {}", requestDTO);

        Client client = clientService.createClient(requestDTO);

        Application application = applicationService.createApplication(client);

        List<LoanOfferDTO> listLoanOffers = feignClient.calculatePossibleLoanOffers(requestDTO);

        listLoanOffers.forEach(loanOfferDTO -> loanOfferDTO.setApplicationId(application.getApplicationId()));

        log.info("[calculatePossibleLoanOffers] << result: {}", listLoanOffers);

        return listLoanOffers;
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

        EmailMassageDTO massageDTO = new EmailMassageDTO(application.getClientId().getEmail(), Theme.finish_registration, application.getApplicationId());

        dealProducer.sendMessage(massageDTO, Theme.finish_registration.name());

        log.info("[chooseOneOfTheOffers] << result: {}", application);
    }


    @Operation(summary = "Completion registration", description = "Completion of registration and calculation of full loan terms")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status history for application has been updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed for some argument. Invalid input supplied"),
            @ApiResponse(responseCode = "404", description = "Not found some resource in database"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping("/calculate/{applicationId}")
    public void completionRegistrationAndCalculateFullCredit(@PathVariable(value = "applicationId") Long applicationId,
                                                             @Valid @RequestBody FinishRegistrationRequestDTO requestDTO) {
        log.info("[completionRegistrationAndCalculateFullCredit] >> applicationId:{}, requestDTO: {}", applicationId, requestDTO);

        Application application = applicationService.getApplicationById(applicationId);

        Client client = clientService.updateClient(application.getClientId().getClientId(), requestDTO);

        ScoringDataDTO scoringDataDTO = creditService.createScoringDataDTO(requestDTO, client, application.getAppliedOffer());

        CreditDTO creditDTO = feignClient.calculateFullLoanParameters(scoringDataDTO);

        Credit credit = creditService.createCredit(creditDTO, application);

        EmailMassageDTO massageDTO = new EmailMassageDTO(application.getClientId().getEmail(), Theme.create_documents, applicationId);

        dealProducer.sendMessage(massageDTO, Theme.create_documents.name());

        log.info("[completionRegistrationAndCalculateFullCredit] << result: {}", credit);
    }


    @Operation(summary = "Request to send documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Possible Loan Offers have been calculated"),
            @ApiResponse(responseCode = "400", description = "Validation failed for some argument"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping("document/{applicationId}/send")
    public void sendDocuments(@PathVariable(value = "applicationId") Long applicationId) {
        log.info("[sendDocuments] >> applicationId:{}", applicationId);

        Application application = applicationService.getApplicationById(applicationId);

        EmailMassageDTO massageDTO = new EmailMassageDTO(application.getClientId().getEmail(), Theme.send_documents, 1L);

        dealProducer.sendMessage(massageDTO, Theme.send_documents.name());

        log.info("[sendDocuments] << result is void, message: {}, topic/theme: {}", massageDTO, Theme.send_documents.name());
    }

    @Operation(summary = "Request to sign documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan Offer have been choose"),
            @ApiResponse(responseCode = "400", description = "Validation failed for some argument"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping("document/{applicationId}/sign")
    public void signDocuments(@PathVariable(value = "applicationId") Long applicationId) {
        log.info("[signDocuments] >> applicationId:{}", applicationId);

        Application application = applicationService.getApplicationById(applicationId);

        EmailMassageDTO massageDTO = new EmailMassageDTO(application.getClientId().getEmail(), Theme.credit_issued, applicationId);

        dealProducer.sendMessage(massageDTO, Theme.credit_issued.name());

        log.info("[signDocuments] << result is void, message: {}, topic/theme: {}", massageDTO, Theme.credit_issued.name());
    }

    @Operation(summary = "Signing of documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan Offer have been choose"),
            @ApiResponse(responseCode = "400", description = "Validation failed for some argument"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping("document/{applicationId}/code")
    public void codeDocuments(@PathVariable(value = "applicationId") Long applicationId) {
        log.info("[codeDocuments] >> applicationId:{}", applicationId);

        Application application = applicationService.getApplicationById(applicationId);

        EmailMassageDTO massageDTO = new EmailMassageDTO(application.getClientId().getEmail(), Theme.send_ses, applicationId);

        dealProducer.sendMessage(massageDTO, Theme.send_ses.name());

        log.info("[codeDocuments] << result is void, message: {}, topic/theme: {}", massageDTO, Theme.send_ses.name());

    }
}
