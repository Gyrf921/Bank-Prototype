package com.bankprototype.gateway.web.controller;

import com.bankprototype.gateway.web.dto.LoanOfferDTO;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


class ApplicationControllerTest extends BaseControllerTest {


    @Test
    void calculatePossibleLoanOffers() throws Exception {

        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();

        List<LoanOfferDTO> listLoanOffers = List.of(enhancedRandom.nextObject(LoanOfferDTO.class),
                enhancedRandom.nextObject(LoanOfferDTO.class),
                enhancedRandom.nextObject(LoanOfferDTO.class),
                enhancedRandom.nextObject(LoanOfferDTO.class));

        when(applicationFeignClient.calculatePossibleLoanOffers(any()))
                .thenReturn(ResponseEntity.ok().body(listLoanOffers));

        ResultActions response = mockMvc.perform(post("/application")
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
                .andExpect(MockMvcResultMatchers.status().isOk());

        System.out.println(response);

    }

    @Test
    void calculatePossibleLoanOffers_ValidationException() throws Exception {
        doNothing().when(applicationFeignClient).chooseOneOfTheOffers(any());

        ResultActions response = mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"amount\": 1000000,\n" +
                                "  \"term\": 6,\n" +
                                "  \"firstName\": \"firstName\",\n" +
                                "  \"lastName\": \"lastName\",\n" +
                                "  \"middleName\": \"middleName\",\n" +
                                "  \"email\": \"notEmail\",\n" +
                                "  \"birthDate\": \"2020-07-07\",\n" +
                                "  \"passportSeries\": \"1111111\",\n" +
                                "  \"passportNumber\": \"222222\"\n" +
                                "}")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());

        System.out.println(response);

    }

    @Test
    void chooseOneOfTheOffers() throws Exception {

        doNothing().when(applicationFeignClient).chooseOneOfTheOffers(any());

        ResultActions response = mockMvc.perform(post("/application/apply")
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
    void completionRegistrationAndCalculateFullCredit() throws Exception {

        doNothing().when(dealFeignClient).completionRegistrationAndCalculateFullCredit(any(), any());

        ResultActions response = mockMvc.perform(put("/application/registration/1")
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
}