package com.bankprototype.application.web.controller;

import com.bankprototype.application.web.dto.LoanOfferDTO;
import com.bankprototype.application.web.feign.DealFeignClient;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationControllerTest {

    @MockBean
    private DealFeignClient feignClient;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void calculatePossibleLoanOffers() throws Exception {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
        LoanOfferDTO ln1 = enhancedRandom.nextObject(LoanOfferDTO.class);
        LoanOfferDTO ln2 = enhancedRandom.nextObject(LoanOfferDTO.class);
        ln1.setRequestedAmount(BigDecimal.valueOf(1000000));
        ln1.setIsSalaryClient(true);
        ln1.setIsInsuranceEnabled(true);

        ln2.setRequestedAmount(BigDecimal.valueOf(1100000));
        ln2.setIsSalaryClient(false);
        ln2.setIsInsuranceEnabled(false);

        List<LoanOfferDTO> list = List.of(ln2, ln2, ln1, ln1);
        ResponseEntity<List<LoanOfferDTO>> listResponseEntity = ResponseEntity.ok(list);
        when(feignClient.calculatePossibleLoanOffers(any()))
                .thenReturn(listResponseEntity);

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
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.[*].requestedAmount", hasItem(1000000)))
                .andExpect(jsonPath("$.[*].requestedAmount", hasItem(1100000)))
                .andExpect(jsonPath("$.[*].isInsuranceEnabled", hasItem(true)))
                .andExpect(jsonPath("$.[*].isInsuranceEnabled", hasItem(false)))
                .andExpect(jsonPath("$.[*].isInsuranceEnabled", hasItem(false)))
                .andExpect(jsonPath("$.[*].isSalaryClient", hasItem(true)))
                .andExpect(jsonPath("$").isArray());

        System.out.println(response);
    }

    @Test
    void calculatePossibleLoanOffers_BirthdateException() throws Exception {

        ResultActions response = mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"amount\": 1000000,\n" +
                                "  \"term\": 6,\n" +
                                "  \"firstName\": \"firstName\",\n" +
                                "  \"lastName\": \"lastName\",\n" +
                                "  \"middleName\": \"middleName\",\n" +
                                "  \"email\": \"string@gmail.com\",\n" +
                                "  \"birthDate\": \"2022-07-07\",\n" +
                                "  \"passportSeries\": \"1111\",\n" +
                                "  \"passportNumber\": \"222222\"\n" +
                                "}")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        System.out.println(response);
    }

    @Test
    void calculatePossibleLoanOffers_ValidException() throws Exception {

        ResultActions response = mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"amount\": 1000000,\n" +
                                "  \"term\": 6,\n" +
                                "  \"firstName\": \"firstName\",\n" +
                                "  \"lastName\": \"lastName\",\n" +
                                "  \"middleName\": \"middleName\",\n" +
                                "  \"email\": \"string@gmail.com\",\n" +
                                "  \"birthDate\": \"2000-07-07\",\n" +
                                "  \"passportSeries\": \"1111231231\",\n" +
                                "  \"passportNumber\": \"2222222\"\n" +
                                "}")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());

        System.out.println(response);
    }


    @Test
    void chooseOneOfTheOffers() throws Exception {

        doNothing().when(feignClient).chooseOneOfTheOffers(any());

        ResultActions response = mockMvc.perform(post("/application/offer")
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
}