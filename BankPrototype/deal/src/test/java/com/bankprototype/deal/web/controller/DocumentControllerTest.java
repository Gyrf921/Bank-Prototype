package com.bankprototype.deal.web.controller;

import com.bankprototype.deal.kafka.EmailMessageDTO;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
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

        ResultActions response = mockMvc.perform(post("/document/1/send"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        System.out.println(response);
    }

    @Test
    void signDocuments() throws Exception {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
        EmailMessageDTO emailMessageDTO = enhancedRandom.nextObject(EmailMessageDTO.class);

        when(dealProducer.createMessage(any(), any()))
                .thenReturn(emailMessageDTO);

        doNothing().when(dealProducer).sendMessage(any(), any());

        ResultActions response = mockMvc.perform(post("/document/1/sign"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        System.out.println(response);
    }

    @Test
    void codeDocuments() throws Exception {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
        EmailMessageDTO emailMessageDTO = enhancedRandom.nextObject(EmailMessageDTO.class);

        when(dealProducer.createMessage(any(), any()))
                .thenReturn(emailMessageDTO);

        doNothing().when(dealProducer).sendMessage(any(), any());

        ResultActions response = mockMvc.perform(post("/document/1/code"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        System.out.println(response);
    }
}