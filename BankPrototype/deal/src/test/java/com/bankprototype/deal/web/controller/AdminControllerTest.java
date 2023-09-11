package com.bankprototype.deal.web.controller;

import com.bankprototype.deal.repository.dao.Application;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class AdminControllerTest extends BaseControllerTest {

    @Test
    void getApplication() throws Exception {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
        Application applicationTest = enhancedRandom.nextObject(Application.class);

        when(applicationService.getApplicationById(any()))
                .thenReturn(applicationTest);

        ResultActions response = mockMvc.perform(get("/deal/admin/application/1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        System.out.println(response);
    }

    @Test
    void getAllApplications() throws Exception {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();

        List<Application> applicationList = List.of(enhancedRandom.nextObject(Application.class),
                enhancedRandom.nextObject(Application.class),
                enhancedRandom.nextObject(Application.class));

        when(applicationService.getAllApplication())
                .thenReturn(applicationList);

        ResultActions response = mockMvc.perform(get("/deal/admin/application"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        System.out.println(response);
    }
}