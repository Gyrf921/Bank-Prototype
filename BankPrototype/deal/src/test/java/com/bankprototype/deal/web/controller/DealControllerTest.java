package com.bankprototype.deal.web.controller;

import com.bankprototype.deal.mapper.StatusHistoryMapper;
import com.bankprototype.deal.repository.ApplicationRepository;
import com.bankprototype.deal.repository.ClientRepository;
import com.bankprototype.deal.repository.dao.Application;
import com.bankprototype.deal.repository.dao.Client;
import com.bankprototype.deal.repository.dao.Credit;
import com.bankprototype.deal.repository.dao.enumfordao.*;
import com.bankprototype.deal.repository.dao.jsonb.Employment;
import com.bankprototype.deal.repository.dao.jsonb.Passport;
import com.bankprototype.deal.repository.dao.jsonb.StatusHistory;
import com.bankprototype.deal.service.ApplicationService;
import com.bankprototype.deal.service.CreditService;
import com.bankprototype.deal.service.impl.ClientServiceImpl;
import com.bankprototype.deal.web.dto.ApplicationStatusHistoryDTO;
import com.bankprototype.deal.web.dto.EmploymentDTO;
import com.bankprototype.deal.web.dto.LoanOfferDTO;
import com.bankprototype.deal.web.dto.ScoringDataDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class DealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientServiceImpl clientService;
    @Mock
    private ApplicationService applicationService;
    @Mock
    private CreditService creditService;

    private static StatusHistoryMapper statusHistoryMapper;

    private static Client clientTest;
    private static Client clientTestUpdated;
    private static Application applicationTest;
    private static Application applicationTestAfter;
    private static ScoringDataDTO scoringDataDTO;
    private static Credit credit;
    @BeforeAll
    static void set(){
        Passport passportTest = Passport.builder()
                .series("1111")
                .number("222222")
                .build();
        ApplicationStatusHistoryDTO applicationStatusHistoryDTO = ApplicationStatusHistoryDTO.builder()
                .status(ApplicationStatus.PREAPPROVAL)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
                .build();
        StatusHistory applicationStatusHistory = StatusHistory.builder()
                .status(ApplicationStatus.PREAPPROVAL)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
                .build();
        List<StatusHistory> listStatus = new LinkedList<>();
        listStatus.add(applicationStatusHistory);

        clientTest = Client.builder()
                .clientId(100L)
                .firstName("firstName")
                .birthDate(LocalDate.of(1990, 07, 07))
                .email("string@gmail.com")
                .dependentAmount(BigDecimal.valueOf(1000000))
                .passport(passportTest)
                .build();

        applicationTest = Application.builder()
                .applicationId(1L)
                .clientId(clientTest)
                .creditId(null)
                .status(ApplicationStatus.PREAPPROVAL.name())
                .creationDate(LocalDateTime.now())
                .appliedOffer(null)
                .signDate(null)
                .sesCode(null)
                .statusHistory(listStatus)
                .build();

        listStatus.add(applicationStatusHistory);
        LoanOfferDTO loanOfferDTO = LoanOfferDTO.builder()
                .applicationId(1L)
                .term(6)
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build();

        ApplicationStatus status = ApplicationStatus.APPROVED;
        Application applicationTestAfter = Application.builder()
                .applicationId(1l)
                .clientId(clientTest)
                .status(status.name())
                .statusHistory(listStatus)
                .appliedOffer(loanOfferDTO)
                .build();

        Passport passportTestUpdated = Passport.builder()
                .series("1111")
                .number("222222")
                .issueDate(LocalDateTime.now())
                .issueBranch("issue Branch")
                .build();

        Employment employment = Employment.builder()
                .status(EmploymentStatus.BUSINESS_OWNER)
                .salary(BigDecimal.valueOf(400000))
                .build();

        EmploymentDTO employmentDTO = EmploymentDTO.builder()
                .employmentStatus(EmploymentStatus.BUSINESS_OWNER)
                .salary(BigDecimal.valueOf(400000))
                .build();
        clientTestUpdated = Client.builder()
                .clientId(100L)
                .firstName("firstName")
                .birthDate(LocalDate.of(1990, 07, 07))
                .email("string@gmail.com")
                .dependentAmount(BigDecimal.valueOf(1000000))
                .passport(passportTestUpdated)
                .gender(Gender.MALE)
                .maritalStatus(MaritalStatus.SINGLE)
                .employment(employment)
                .account("zsdfg")
                .build();

        ScoringDataDTO scoringDataDTO = ScoringDataDTO.builder()
                .amount(loanOfferDTO.getRequestedAmount())
                .term(loanOfferDTO.getTerm())
                .firstName(clientTestUpdated.getFirstName())
                .lastName(clientTestUpdated.getLastName())
                .middleName(clientTestUpdated.getMiddleName())
                .gender(clientTestUpdated.getGender())
                .birthdate(clientTestUpdated.getBirthDate())
                .passportSeries(clientTestUpdated.getPassport().getSeries())
                .passportNumber(clientTestUpdated.getPassport().getNumber())
                .passportIssueDate(clientTestUpdated.getPassport().getIssueDate().toLocalDate())
                .passportIssueBranch(clientTestUpdated.getPassport().getIssueBranch())
                .maritalStatus(clientTestUpdated.getMaritalStatus())
                .dependentAmount(clientTestUpdated.getDependentAmount())
                .employment(employmentDTO)
                .account(clientTestUpdated.getAccount())
                .isInsuranceEnabled(loanOfferDTO.getIsInsuranceEnabled())
                .isSalaryClient(loanOfferDTO.getIsSalaryClient())
                .build();
    }

    @Test
    void calculatePossibleLoanOffers() throws Exception {

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

        when(applicationService.getApplicationById(any()))
                .thenReturn(applicationTestAfter);

        when(clientService.updateClient(any(),any()))
                .thenReturn(clientTestUpdated);

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