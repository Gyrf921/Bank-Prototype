package com.bankprototype.deal.web.controller;

import com.bankprototype.deal.web.kafka.EmailMessageDTO;
import com.bankprototype.deal.dao.Application;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


class DocumentControllerTest extends BaseControllerTest {


    @Test
    void sendDocuments() throws Exception {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
        EmailMessageDTO emailMessageDTO = enhancedRandom.nextObject(EmailMessageDTO.class);

        when(dealProducer.createMessage(any(), any()))
                .thenReturn(emailMessageDTO);

        doNothing().when(dealProducer).sendMessage(any(), any());

        ResultActions response = mockMvc.perform(post("/deal/document/1/send"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        System.out.println(response);
    }

    @Test
    void signDocuments() throws Exception {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
        EmailMessageDTO emailMessageDTO = enhancedRandom.nextObject(EmailMessageDTO.class);
        Application application = enhancedRandom.nextObject(Application.class);

        when(applicationService.updateSesCodeForApplication(any()))
                .thenReturn(application);

        when(dealProducer.createMessage(any(), any()))
                .thenReturn(emailMessageDTO);

        doNothing().when(dealProducer).sendMessage(any(), any());

        ResultActions response = mockMvc.perform(post("/deal/document/1/sign"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        System.out.println(response);
    }

    @Test
    void codeDocuments() throws Exception {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
        EmailMessageDTO emailMessageDTO = enhancedRandom.nextObject(EmailMessageDTO.class);

        when(applicationService.checkingCorrectnessSesCode(any(), any()))
                .thenReturn(true);

        when(dealProducer.createMessage(any(), any()))
                .thenReturn(emailMessageDTO);

        doNothing().when(dealProducer).sendMessage(any(), any());

        ResultActions response = mockMvc.perform(post("/deal/document/1/code?sesCode=666666"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        System.out.println(response);
    }

    @Test
    void codeDocuments_SesCodeIsNotCorrectException() throws Exception {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
        EmailMessageDTO emailMessageDTO = enhancedRandom.nextObject(EmailMessageDTO.class);

        when(applicationService.checkingCorrectnessSesCode(any(), any()))
                .thenReturn(false);

        when(dealProducer.createMessage(any(), any()))
                .thenReturn(emailMessageDTO);

        doNothing().when(dealProducer).sendMessage(any(), any());

        ResultActions response = mockMvc.perform(post("/deal/document/1/code?sesCode=666666"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        System.out.println(response);
    }
}