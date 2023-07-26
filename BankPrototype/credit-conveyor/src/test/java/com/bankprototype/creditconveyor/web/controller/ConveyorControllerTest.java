package com.bankprototype.creditconveyor.web.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;


@SpringBootTest
@AutoConfigureMockMvc //simulating a http request without a server
class ConveyorControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

    }

    @Test
    void getInfoAboutMeeting() throws Exception {

        ResultActions response = mockMvc.perform(post("/conveyor/offers")
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
                .andExpect(MockMvcResultMatchers.status().isOk());

        System.out.println(response);

    }

    @Test
    void createMeeting() {
    }
}