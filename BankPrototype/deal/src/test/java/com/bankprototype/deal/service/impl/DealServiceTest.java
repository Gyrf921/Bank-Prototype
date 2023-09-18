package com.bankprototype.deal.service.impl;

import com.bankprototype.deal.kafka.EmailMessageDTO;
import com.bankprototype.deal.repository.dao.Application;
import com.bankprototype.deal.repository.dao.Client;
import com.bankprototype.deal.repository.dao.Credit;
import com.bankprototype.deal.repository.dao.enumfordao.ApplicationStatus;
import com.bankprototype.deal.service.ClientService;
import com.bankprototype.deal.service.DealProducer;
import com.bankprototype.deal.web.dto.*;
import com.bankprototype.deal.web.feign.CreditConveyorFeignClient;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DealServiceTest extends BaseServiceTest {

    @Mock
    protected DealProducer dealProducer;

    @Mock
    protected CreditConveyorFeignClient feignClient;

    @Mock
    protected ClientService clientService;

    @Mock
    protected CreditServiceImpl creditService;

    @Mock
    protected ApplicationServiceImpl applicationService;

    @InjectMocks
    private DealService dealService;

    @Test
    void createLoanApplication() {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();

        LoanApplicationRequestDTO requestDTO = enhancedRandom.nextObject(LoanApplicationRequestDTO.class);
        Client clientTest = enhancedRandom.nextObject(Client.class);
        Application applicationTest = enhancedRandom.nextObject(Application.class);
        applicationTest.setApplicationId(0L);

        LoanOfferDTO ln1 = enhancedRandom.nextObject(LoanOfferDTO.class);
        ln1.setRequestedAmount(BigDecimal.valueOf(1000000));
        ln1.setIsSalaryClient(true);
        ln1.setIsInsuranceEnabled(true);

        List<LoanOfferDTO> list = List.of(ln1, ln1, ln1, ln1);
        ResponseEntity<List<LoanOfferDTO>> listResponseEntity = ResponseEntity.ok().body(list);

        when(clientService.createClient(any()))
                .thenReturn(clientTest);

        when(applicationService.createApplication(any()))
                .thenReturn(applicationTest);

        when(feignClient.calculatePossibleLoanOffers(any()))
                .thenReturn(listResponseEntity);

        List<LoanOfferDTO> loanOfferDTO = dealService.createLoanApplication(requestDTO);

        System.out.println(loanOfferDTO);

        assertThat(loanOfferDTO.toArray()).hasSize(4);

        verify(clientService, times(1)).createClient(any());
        verify(applicationService, times(1)).createApplication(any());
    }

    @Test
    void chooseOneOfTheOffers() {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();

        LoanOfferDTO requestDTO = enhancedRandom.nextObject(LoanOfferDTO.class);

        Application applicationTest = enhancedRandom.nextObject(Application.class);
        applicationTest.setStatus(ApplicationStatus.PREAPPROVAL);

        when(applicationService.updateStatusHistoryForApplication(any(), any()))
                .thenReturn(applicationTest);

        doNothing().when(dealProducer).sendMessage(any(), any());

        boolean isItWork = dealService.chooseOneOfTheOffers(requestDTO);

        assertThat(isItWork).isTrue();
        verify(applicationService, times(1)).updateStatusHistoryForApplication(any(), any());
    }

    @Test
    void finishRegistration() {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();

        ScoringDataDTO scoringDataDTO = enhancedRandom.nextObject(ScoringDataDTO.class);
        Client clientTest = enhancedRandom.nextObject(Client.class);
        clientTest.setClientId(1L);

        Application applicationTest = Application.builder()
                .applicationId(1L)
                .clientId(clientTest).build();

        Credit credit = enhancedRandom.nextObject(Credit.class);
        CreditDTO creditDTO = enhancedRandom.nextObject(CreditDTO.class);
        FinishRegistrationRequestDTO finishRegistrationRequestDTO = enhancedRandom.nextObject(FinishRegistrationRequestDTO.class);
        EmailMessageDTO emailMessageDTO = enhancedRandom.nextObject(EmailMessageDTO.class);
        ResponseEntity<CreditDTO> creditDTOResponseEntity = ResponseEntity.ok().body(creditDTO);

        when(applicationService.getApplicationById(any()))
                .thenReturn(applicationTest);

        when(clientService.updateClient(any(), any()))
                .thenReturn(clientTest);

        when(creditService.createScoringDataDTO(any(), any(), any()))
                .thenReturn(scoringDataDTO);

        when(feignClient.calculateFullLoanParameters(any()))
                .thenReturn(creditDTOResponseEntity);

        when(creditService.createCredit(any(), any()))
                .thenReturn(credit);

        when(dealProducer.createMessage(any(), any()))
                .thenReturn(emailMessageDTO);

        doNothing().when(dealProducer).sendMessage(any(), any());

        boolean isItWork = dealService.finishRegistration(1L, finishRegistrationRequestDTO);

        assertThat(isItWork).isTrue();
        verify(applicationService, times(1)).getApplicationById(any());
        verify(clientService, times(1)).updateClient(any(), any());
        verify(creditService, times(1)).createScoringDataDTO(any(), any(), any());
        verify(feignClient, times(1)).calculateFullLoanParameters(any());
        verify(dealProducer, times(1)).sendMessage(any(), any());
    }
}