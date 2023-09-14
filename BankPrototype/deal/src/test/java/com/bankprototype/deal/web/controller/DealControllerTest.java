package com.bankprototype.deal.web.controller;

import com.bankprototype.deal.exception.ResourceNotFoundException;
import com.bankprototype.deal.service.impl.DealService;
import com.bankprototype.deal.web.dto.LoanOfferDTO;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
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
    private DealService dealService;

    @Test
    void calculatePossibleLoanOffers() throws Exception {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
        LoanOfferDTO ln1 = enhancedRandom.nextObject(LoanOfferDTO.class);
        List<LoanOfferDTO> list = List.of(ln1, ln1, ln1, ln1);
        ln1.setRequestedAmount(BigDecimal.valueOf(1000000));
        ln1.setIsSalaryClient(true);
        ln1.setIsInsuranceEnabled(true);
        when(dealService.createLoanApplication(any()))
                .thenReturn(list);

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

        when(dealService.chooseOneOfTheOffers(any())).thenReturn(true);

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

        when(dealService.chooseOneOfTheOffers(any()))
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

        ResultActions response = mockMvc.perform(put("/deal/calculate/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"gender\": \"MALE\",\n" +
                                "  \"maritalStatus\": \"SINGLE\",\n" +
                                "  \"dependentAmount\": 1000000,\n" +
                                "  \"passportIssueDate\": \"2020-07-30\",\n" +
                                "  \"passportIssueBranch\": \"passportIssue\",\n" +
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

        when(dealService.finishRegistration(any(), any()))
                .thenThrow(ResourceNotFoundException.class);

        doNothing().when(dealProducer).sendMessage(any(), any());

        ResultActions response = mockMvc.perform(put("/deal/calculate/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"gender\": \"MALE\",\n" +
                                "  \"maritalStatus\": \"SINGLE\",\n" +
                                "  \"dependentAmount\": 1000000,\n" +
                                "  \"passportIssueDate\": \"2020-07-30\",\n" +
                                "  \"passportIssueBranch\": \"passportIssue\",\n" +
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