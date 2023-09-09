package com.bankprototype.creditconveyor.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc //simulating a http request without a server
class ConveyorControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Test
    void calculatePossibleLoanOffers() throws Exception {

        ResultActions response = mockMvc.perform(post("/conveyor/offers")
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
    void calculateFullLoanParameters() throws Exception {
        ResultActions response = mockMvc.perform(post("/conveyor/calculation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"amount\": 1000000,\n" +
                                "  \"term\": 6,\n" +
                                "  \"firstName\": \"firstName\",\n" +
                                "  \"lastName\": \"lastName\",\n" +
                                "  \"middleName\": \"middleName\",\n" +
                                "  \"gender\": \"MALE\",\n" +
                                "  \"birthDate\": \"2000-07-30\",\n" +
                                "  \"passportSeries\": \"1111\",\n" +
                                "  \"passportNumber\": \"222222\",\n" +
                                "  \"passportIssueDate\": \"2020-07-30\",\n" +
                                "  \"passportIssueBranch\": \"passportIssueBranch\",\n" +
                                "  \"maritalStatus\": \"SINGLE\",\n" +
                                "  \"dependentAmount\": 0,\n" +
                                "  \"employment\": {\n" +
                                "    \"employmentStatus\": \"SELF_EMPLOYED\",\n" +
                                "    \"employerINN\": \"string\",\n" +
                                "    \"salary\": 100000,\n" +
                                "    \"position\": \"MIDDLE_MANAGER\",\n" +
                                "    \"workExperienceTotal\": 48,\n" +
                                "    \"workExperienceCurrent\": 24\n" +
                                "  },\n" +
                                "  \"account\": \"string\",\n" +
                                "  \"isInsuranceEnabled\": false,\n" +
                                "  \"isSalaryClient\": false\n" +
                                "}")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.amount").value(1000000))
                .andExpect(jsonPath("$.term").value(6))
                .andExpect(jsonPath("$.rate").value(8.0))
                .andExpect(jsonPath("$.isInsuranceEnabled").value(false))
                .andExpect(jsonPath("$.isSalaryClient").value(false))
                .andExpect(jsonPath("$.paymentSchedule.[*].number", hasItem(1)));

        System.out.println(response);
    }

    @Test
    void calculateFullLoanParametersExceptionUserIsUNEMPLOYED() throws Exception {
        ResultActions response = mockMvc.perform(post("/conveyor/calculation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"amount\": 1000000,\n" +
                                "  \"term\": 6,\n" +
                                "  \"firstName\": \"firstName\",\n" +
                                "  \"lastName\": \"lastName\",\n" +
                                "  \"middleName\": \"middleName\",\n" +
                                "  \"gender\": \"MALE\",\n" +
                                "  \"birthDate\": \"2000-07-30\",\n" +
                                "  \"passportSeries\": \"1111\",\n" +
                                "  \"passportNumber\": \"222222\",\n" +
                                "  \"passportIssueDate\": \"2020-07-30\",\n" +
                                "  \"passportIssueBranch\": \"passportIssueBranch\",\n" +
                                "  \"maritalStatus\": \"SINGLE\",\n" +
                                "  \"dependentAmount\": 0,\n" +
                                "  \"employment\": {\n" +
                                "    \"employmentStatus\": \"UNEMPLOYED\",\n" +
                                "    \"employerINN\": \"string\",\n" +
                                "    \"salary\": 100000,\n" +
                                "    \"position\": \"MIDDLE_MANAGER\",\n" +
                                "    \"workExperienceTotal\": 48,\n" +
                                "    \"workExperienceCurrent\": 24\n" +
                                "  },\n" +
                                "  \"account\": \"string\",\n" +
                                "  \"isInsuranceEnabled\": false,\n" +
                                "  \"isSalaryClient\": false\n" +
                                "}")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        System.out.println(response);
    }


}