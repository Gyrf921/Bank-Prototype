package com.bankprototype.gateway.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class DocumentControllerTest extends BaseControllerTest {

    @Test
    void sendDocuments() throws Exception {
        doNothing().when(dealFeignClient).sendDocuments(any());

        ResultActions response = mockMvc.perform(post("/document/1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        System.out.println(response);
    }

    @Test
    void signDocuments() throws Exception {
        doNothing().when(dealFeignClient).signDocuments(any());

        ResultActions response = mockMvc.perform(post("/document/1/sign"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        System.out.println(response);
    }

    @Test
    void codeDocuments() throws Exception {
        doNothing().when(dealFeignClient).codeDocuments(any(), any());

        ResultActions response = mockMvc.perform(post("/document/1/code?sesCode=666666"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        System.out.println(response);
    }
}