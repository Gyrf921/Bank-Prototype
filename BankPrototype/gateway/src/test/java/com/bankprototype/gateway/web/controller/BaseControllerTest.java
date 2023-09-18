package com.bankprototype.gateway.web.controller;

import com.bankprototype.gateway.web.feign.ApplicationFeignClient;
import com.bankprototype.gateway.web.feign.DealFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected DealFeignClient dealFeignClient;

    @MockBean
    protected ApplicationFeignClient applicationFeignClient;
}
