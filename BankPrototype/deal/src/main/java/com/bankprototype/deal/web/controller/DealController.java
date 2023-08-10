package com.bankprototype.deal.web.controller;

import com.bankprototype.deal.repository.dao.Credit;
import com.bankprototype.deal.service.ApplicationService;
import com.bankprototype.deal.service.ClientService;
import com.bankprototype.deal.service.CreditService;
import com.bankprototype.deal.service.impl.ApplicationServiceImpl;
import com.bankprototype.deal.service.impl.ClientServiceImpl;
import com.bankprototype.deal.service.impl.CreditServiceImpl;
import com.bankprototype.deal.web.dto.*;
import com.bankprototype.deal.repository.dao.Application;
import com.bankprototype.deal.repository.dao.Client;
import com.bankprototype.deal.repository.dao.enumfordao.ApplicationStatus;
import com.bankprototype.deal.web.feign.CreditConveyorFeignClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@Slf4j
@RequestMapping("/deal")
public class DealController {
    private final CreditConveyorFeignClient feignClient;

    private final ClientService clientService;
    private final ApplicationService applicationService;
    private final CreditService creditService;

    public DealController(CreditConveyorFeignClient feignClient, ClientServiceImpl clientService, ApplicationServiceImpl applicationService, CreditServiceImpl creditService) {
        this.feignClient = feignClient;
        this.clientService = clientService;
        this.applicationService = applicationService;
        this.creditService = creditService;
    }

    @Operation(summary = "Calculate 4 loan offers")
    @ApiResponses(value = { @ApiResponse(responseCode = "500", description = "Validation failed for some argument") })
    @PostMapping("/application")
    public List<LoanOfferDTO> calculatePossibleLoanOffers(@Valid @RequestBody LoanApplicationRequestDTO requestDTO)
    {
        log.info("[calculatePossibleLoanOffers] >> requestDTO: {}", requestDTO);

        Client client = clientService.createClient(requestDTO);

        Application application = applicationService.createApplication(client.getClientId());

        List<LoanOfferDTO> listLoanOffers = feignClient.calculatePossibleLoanOffers(requestDTO);

        listLoanOffers.forEach(loanOfferDTO -> loanOfferDTO.setApplicationId(application.getApplicationId()));

        log.info("[calculatePossibleLoanOffers] << result: {}", listLoanOffers);

        return listLoanOffers;
    }

    @Operation(summary = "Choose one of the loan offers")
    @ApiResponses(value = { @ApiResponse(responseCode = "404", description = "Not found application") })
    @PutMapping("/offer")
    public void chooseOneOfTheOffers(@RequestBody LoanOfferDTO loanOfferDTO)
    {
        log.info("[chooseOneOfTheOffers] >> loanOfferDTO: {}", loanOfferDTO);

        Application application = applicationService
                .updateStatusHistoryForApplication(loanOfferDTO, ApplicationStatus.PREAPPROVAL);

        log.info("[chooseOneOfTheOffers] << result: {}", application);
    }

    @Operation(summary = "Completion registration", description = "Completion of registration and calculation of full loan terms")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Not found some resource in database"),
            @ApiResponse(responseCode = "500", description = "Validation failed for some argument") })
    @PostMapping("/calculate/{applicationId}")
    public void completionRegistrationAndCalculateFullCredit(@PathVariable(value = "applicationId") Long applicationId,
                                                             @Valid @RequestBody FinishRegistrationRequestDTO requestDTO)
    {
        log.info("[completionRegistrationAndCalculateFullCredit] >> applicationId:{}, requestDTO: {}", applicationId, requestDTO);

        Application application = applicationService.getApplicationById(applicationId);

        Client client = clientService.updateClient(application.getClientId(), requestDTO);

        ScoringDataDTO scoringDataDTO = creditService.createScoringDataDTO(requestDTO, client, application.getAppliedOffer());

        CreditDTO creditDTO = feignClient.calculateFullLoanParameters(scoringDataDTO);

        Credit credit = creditService.createCredit(creditDTO);

        log.info("[completionRegistrationAndCalculateFullCredit] << result: {}", credit);

    }
}
