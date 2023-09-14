package com.bankprototype.deal.web.controller;

import com.bankprototype.deal.repository.ApplicationRepository;
import com.bankprototype.deal.repository.ClientRepository;
import com.bankprototype.deal.repository.CreditRepository;
import com.bankprototype.deal.service.ClientService;
import com.bankprototype.deal.service.DealProducer;
import com.bankprototype.deal.service.impl.ApplicationServiceImpl;
import com.bankprototype.deal.service.impl.CreditServiceImpl;
import com.bankprototype.deal.web.feign.CreditConveyorFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@EnableAutoConfiguration(exclude = {HibernateJpaAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class})
public abstract class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected DealProducer dealProducer;

    @MockBean
    protected ApplicationRepository applicationRepository;

    @MockBean
    protected ClientRepository clientRepository;

    @MockBean
    protected CreditRepository creditRepository;

    @MockBean
    protected ApplicationServiceImpl applicationService;

    @MockBean
    protected CreditConveyorFeignClient feignClient;

    @MockBean
    protected ClientService clientService;

    @MockBean
    protected CreditServiceImpl creditService;


}
