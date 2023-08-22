package com.bankprototype.deal.web.controller;

import com.bankprototype.deal.exception.ResourceNotFoundException;
import com.bankprototype.deal.mapper.*;
import com.bankprototype.deal.repository.ApplicationRepository;
import com.bankprototype.deal.repository.ClientRepository;
import com.bankprototype.deal.repository.dao.Application;
import com.bankprototype.deal.repository.dao.Client;
import com.bankprototype.deal.repository.dao.Credit;
import com.bankprototype.deal.repository.dao.enumfordao.*;
import com.bankprototype.deal.repository.dao.jsonb.Employment;
import com.bankprototype.deal.repository.dao.jsonb.Passport;
import com.bankprototype.deal.repository.dao.jsonb.StatusHistory;
import com.bankprototype.deal.service.*;
import com.bankprototype.deal.service.impl.ApplicationServiceImpl;
import com.bankprototype.deal.service.impl.ClientServiceImpl;
import com.bankprototype.deal.service.impl.CreditServiceImpl;
import com.bankprototype.deal.web.dto.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class DealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @MockBean
    private ApplicationServiceImpl applicationService;

    @MockBean
    private CreditServiceImpl creditService;


    @Test
    void calculatePossibleLoanOffers() throws Exception {

        Client clientTest = random(Client.class);
        Application applicationTest = random(Application.class);

        when(clientService.createClient(any()))
                .thenReturn(clientTest);
        when(applicationService.createApplication(any()))
                .thenReturn(applicationTest);

        ResultActions response = mockMvc.perform(post("/deal/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"amount\": 1000000,\n" +
                                "  \"term\": 6,\n" +
                                "  \"firstName\": \"firstName\",\n" +
                                "  \"lastName\": \"lastName\",\n" +
                                "  \"middleName\": \"middleName\",\n" +
                                "  \"email\": \"string@gmail.com\",\n" +
                                "  \"birthdate\": \"1990-07-07\",\n" +
                                "  \"passportSeries\": \"1111\",\n" +
                                "  \"passportNumber\": \"222222\"\n" +
                                "}")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.[*].requestedAmount", hasItem(1000000)))
                .andExpect(jsonPath("$.[*].requestedAmount", hasItem(1100000)))
                .andExpect(jsonPath("$.[*].totalAmount", hasItem(1159689)))
                .andExpect(jsonPath("$.[*].monthlyPayment", hasItem(16107)))
                .andExpect(jsonPath("$.[*].rate", hasItem(5.0)))
                .andExpect(jsonPath("$.[*].isInsuranceEnabled", hasItem(false)))
                .andExpect(jsonPath("$.[*].isSalaryClient", hasItem(false)))
                .andExpect(jsonPath("$.[*].isInsuranceEnabled", hasItem(true)))
                .andExpect(jsonPath("$.[*].isSalaryClient", hasItem(true)));

        System.out.println(response);
    }

    @Test
    void chooseOneOfTheOffers() throws Exception {
        Application applicationTestAfter = random(Application.class);

        when(applicationService.updateStatusHistoryForApplication(any(),any()))
                .thenReturn(applicationTestAfter);

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

        when(applicationService.updateStatusHistoryForApplication(any(),any()))
                .thenThrow(ResourceNotFoundException.class);

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
                .birthdate(LocalDate.of(1990, 07, 07))
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
        List<StatusHistory> listStatus = List.of(applicationStatusHistory);

        Application applicationTest = Application.builder()
                .applicationId(1L)
                .applicationId(1L).build();

        Client clientTest = random(Client.class);
        clientTest.setClientId(1L);

        Credit credit = new Credit();

        when(applicationService.getApplicationById(any()))
                .thenReturn(applicationTest);

        when(clientService.updateClient(any(),any()))
                .thenReturn(clientTest);

        when(creditService.createScoringDataDTO(any(),any(),any()))
                .thenReturn(scoringDataDTO);

        when(creditService.createCredit(any(),any()))
                .thenReturn(credit);

        ResultActions response = mockMvc.perform(post("/deal/calculate/1")
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

        ResultActions response = mockMvc.perform(post("/deal/calculate/0")
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