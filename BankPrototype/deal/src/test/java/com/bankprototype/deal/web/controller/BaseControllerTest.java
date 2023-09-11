package com.bankprototype.deal.web.controller;

import com.bankprototype.deal.repository.ApplicationRepository;
import com.bankprototype.deal.repository.ClientRepository;
import com.bankprototype.deal.repository.CreditRepository;
import com.bankprototype.deal.service.DealProducer;
import com.bankprototype.deal.service.impl.ApplicationServiceImpl;
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
    public ApplicationRepository applicationRepository;

    @MockBean
    public ClientRepository clientRepository;

    @MockBean
    public CreditRepository creditRepository;

    @MockBean
    public ApplicationServiceImpl applicationService;

}
