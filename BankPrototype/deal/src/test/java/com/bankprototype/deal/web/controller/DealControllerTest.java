package com.bankprototype.deal.web.controller;

import com.bankprototype.deal.exception.ResourceNotFoundException;
import com.bankprototype.deal.repository.dao.Application;
import com.bankprototype.deal.repository.dao.Client;
import com.bankprototype.deal.repository.dao.Credit;
import com.bankprototype.deal.repository.dao.enumfordao.*;
import com.bankprototype.deal.repository.dao.jsonb.StatusHistory;
import com.bankprototype.deal.service.ClientService;
import com.bankprototype.deal.service.impl.ApplicationServiceImpl;
import com.bankprototype.deal.service.impl.CreditServiceImpl;
import com.bankprototype.deal.web.dto.CreditDTO;
import com.bankprototype.deal.web.dto.EmploymentDTO;
import com.bankprototype.deal.web.dto.LoanOfferDTO;
import com.bankprototype.deal.web.dto.ScoringDataDTO;
import com.bankprototype.deal.web.feign.CreditConveyorFeignClient;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


class DealControllerTest extends BaseControllerTest {


    @MockBean
    private CreditConveyorFeignClient feignClient;

    @MockBean
    private ClientService clientService;

    @MockBean
    private ApplicationServiceImpl applicationService;

    @MockBean
    private CreditServiceImpl creditService;

    @Test
    void calculatePossibleLoanOffers() throws Exception {

        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
        Client clientTest = enhancedRandom.nextObject(Client.class);
        Application applicationTest = enhancedRandom.nextObject(Application.class);

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

        ResultActions response = mockMvc.perform(post("/deal/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"amount\": 1000000,\n" +
                                "  \"term\": 6,\n" +
                                "  \"firstName\": \"firstName\",\n" +
                                "  \"lastName\": \"lastName\",\n" +
                                "  \"middleName\": \"middleName\",\n" +
                                "  \"email\": \"string@gmail.com\",\n" +
                                "  \"birthDate\": \"1990-07-07\",\n" +
                                "  \"passportSeries\": \"1111\",\n" +
                                "  \"passportNumber\": \"222222\"\n" +
                                "}")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.[*].requestedAmount", hasItem(1000000)))
                .andExpect(jsonPath("$.[*].isInsuranceEnabled", hasItem(true)))
                .andExpect(jsonPath("$.[*].isSalaryClient", hasItem(true)));

        System.out.println(response);
    }

    @Test
    void chooseOneOfTheOffers() throws Exception {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();

        Application applicationTest = enhancedRandom.nextObject(Application.class);

        when(applicationService.updateStatusHistoryForApplication(any(), any()))
                .thenReturn(applicationTest);

        doNothing().when(dealProducer).sendMessage(any(), any());

        ResultActions response = mockMvc.perform(put("/deal/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"applicationId\": 1,\n" +
                                "  \"requestedAmount\": 1000000,\n" +
                                "  \"totalAmount\": 1159689,\n" +
                                "  \"term\": 6,\n" +
                                "  \"monthlyPayment\": 16107,\n" +
                                "  \"rate\": 5.0,\n" +
                                "  \"isInsuranceEnabled\": true,\n" +
                                "  \"isSalaryClient\": true\n" +
                                "}")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        System.out.println(response);
    }

    @Test
    void chooseOneOfTheOffers_ExceptionResourceNotFound() throws Exception {

        when(applicationService.updateStatusHistoryForApplication(any(), any()))
                .thenThrow(ResourceNotFoundException.class);

        doNothing().when(dealProducer).sendMessage(any(), any());

        ResultActions response = mockMvc.perform(put("/deal/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"applicationId\": 0,\n" +
                                "  \"requestedAmount\": 1000000,\n" +
                                "  \"totalAmount\": 1159689,\n" +
                                "  \"term\": 6,\n" +
                                "  \"monthlyPayment\": 16107,\n" +
                                "  \"rate\": 5.0,\n" +
                                "  \"isInsuranceEnabled\": true,\n" +
                                "  \"isSalaryClient\": true\n" +
                                "}")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        System.out.println(response);
    }

    @Test
    void completionRegistrationAndCalculateFullCredit() throws Exception {

        EmploymentDTO employmentDTO = EmploymentDTO.builder()
                .employmentStatus(EmploymentStatus.BUSINESS_OWNER)
                .salary(BigDecimal.valueOf(400000))
                .build();

        ScoringDataDTO scoringDataDTO = ScoringDataDTO.builder()
                .amount(BigDecimal.valueOf(1000000))
                .term(6)
                .firstName("testName")
                .lastName("testName")
                .middleName("testName")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1990, 07, 07))
                .passportSeries("1111")
                .passportNumber("222222")
                .passportIssueDate(LocalDate.of(2020, 07, 07))
                .passportIssueBranch("issue Branch")
                .maritalStatus(MaritalStatus.SINGLE)
                .dependentAmount(BigDecimal.valueOf(1159689))
                .employment(employmentDTO)
                .account("testAccount")
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build();

        StatusHistory applicationStatusHistory = StatusHistory.builder()
                .status(ApplicationStatus.PREAPPROVAL)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
                .build();

        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();

        Client clientTest = enhancedRandom.nextObject(Client.class);
        clientTest.setClientId(1L);

        Application applicationTest = Application.builder()
                .applicationId(1L)
                .clientId(clientTest).build();

        Credit credit = enhancedRandom.nextObject(Credit.class);
        CreditDTO creditDTO = enhancedRandom.nextObject(CreditDTO.class);
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

        doNothing().when(dealProducer).sendMessage(any(), any());

        ResultActions response = mockMvc.perform(put("/deal/calculate/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"gender\": \"MALE\",\n" +
                                "  \"maritalStatus\": \"SINGLE\",\n" +
                                "  \"dependentAmount\": 1000000,\n" +
                                "  \"passportIssueDate\": \"2020-07-30\",\n" +
                                "  \"passportIssueBrach\": \"passportIssue\",\n" +
                                "  \"employment\": {\n" +
                                "    \"employmentStatus\": \"EMPLOYED\",\n" +
                                "    \"employerINN\": \"string\",\n" +
                                "    \"salary\": 100000,\n" +
                                "    \"position\": \"MIDDLE_MANAGER\",\n" +
                                "    \"workExperienceTotal\": 48,\n" +
                                "    \"workExperienceCurrent\": 24\n" +
                                "  },\n" +
                                "  \"account\": \"account\"\n" +
                                "}")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        System.out.println(response);
    }

    @Test
    void completionRegistrationAndCalculateFullCredit_ExceptionResourceNotFound() throws Exception {

        when(applicationService.getApplicationById(any()))
                .thenThrow(ResourceNotFoundException.class);

        doNothing().when(dealProducer).sendMessage(any(), any());

        ResultActions response = mockMvc.perform(put("/deal/calculate/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"gender\": \"MALE\",\n" +
                                "  \"maritalStatus\": \"SINGLE\",\n" +
                                "  \"dependentAmount\": 1000000,\n" +
                                "  \"passportIssueDate\": \"2020-07-30\",\n" +
                                "  \"passportIssueBrach\": \"passportIssue\",\n" +
                                "  \"employment\": {\n" +
                                "    \"employmentStatus\": \"EMPLOYED\",\n" +
                                "    \"employerINN\": \"string\",\n" +
                                "    \"salary\": 100000,\n" +
                                "    \"position\": \"MIDDLE_MANAGER\",\n" +
                                "    \"workExperienceTotal\": 48,\n" +
                                "    \"workExperienceCurrent\": 24\n" +
                                "  },\n" +
                                "  \"account\": \"account\"\n" +
                                "}")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        System.out.println(response);
    }


}