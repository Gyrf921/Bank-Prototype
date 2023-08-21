package com.bankprototype.deal.service.impl;

import com.bankprototype.deal.repository.ApplicationRepository;
import com.bankprototype.deal.repository.dao.Application;
import com.bankprototype.deal.repository.dao.Client;
import com.bankprototype.deal.repository.dao.Credit;
import com.bankprototype.deal.repository.dao.enumfordao.CreditStatus;
import com.bankprototype.deal.repository.dao.enumfordao.EmploymentStatus;
import com.bankprototype.deal.repository.dao.enumfordao.Gender;
import com.bankprototype.deal.repository.dao.jsonb.Passport;
import com.bankprototype.deal.mapper.*;
import com.bankprototype.deal.web.dto.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest
class CreditServiceImplTest {

    @Mock
    private ApplicationRepository applicationRepository;

    private CreditServiceImpl creditService = new CreditServiceImpl(applicationRepository, new CreditMapperImpl());

    @BeforeAll
    static void set(){

    }

    @Test
    void createScoringDataDTO() {
        Long clientId = 1L;
        EmploymentDTO employmentDTO = EmploymentDTO.builder()
                .employmentStatus(EmploymentStatus.BUSINESS_OWNER)
                .salary(BigDecimal.valueOf(400000))
                .build();
        Passport passportTest = Passport.builder()
                .series("2222")
                .number("111111")
                .build();

        FinishRegistrationRequestDTO requestDTO = FinishRegistrationRequestDTO.builder()
                .dependentAmount(BigDecimal.valueOf(1000000))
                .gender(Gender.MALE)
                .employment(employmentDTO)
                .build();

        Client clientTest = Client.builder()
                .clientId(clientId)
                .firstName("testFirstName")
                .passport(passportTest)
                .birthDate(LocalDate.of(1991, 1, 1))
                .build();

        LoanOfferDTO loanOfferDTO = LoanOfferDTO.builder()
                .applicationId(1L)
                .requestedAmount(requestDTO.getDependentAmount())
                .term(6)
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build();


        ScoringDataDTO scoringDataDTO = creditService.createScoringDataDTO(requestDTO, clientTest, loanOfferDTO);

        System.out.println(scoringDataDTO);

        assertThat(scoringDataDTO).isNotNull();
        assertEquals(scoringDataDTO.getEmployment(), employmentDTO);
        assertEquals(scoringDataDTO.getGender(), requestDTO.getGender());
        assertEquals(scoringDataDTO.getDependentAmount(), requestDTO.getDependentAmount());
        assertEquals(scoringDataDTO.getFirstName(), clientTest.getFirstName());
        assertEquals(scoringDataDTO.getFirstName(), clientTest.getFirstName());
        assertEquals(scoringDataDTO.getIsInsuranceEnabled(), true);
        assertEquals(scoringDataDTO.getIsSalaryClient(), true);

    }

    @Test
    void createCredit() {

        PaymentScheduleElement payment1 = PaymentScheduleElement.builder()
                .totalPayment(BigDecimal.valueOf(16101)).build();
        PaymentScheduleElement payment2 = PaymentScheduleElement.builder()
                .totalPayment(BigDecimal.valueOf(16101)).build();
        List<PaymentScheduleElement> paymentSchedule = new LinkedList<>();
        paymentSchedule.add(payment1);
        paymentSchedule.add(payment2);

       Application application = Application.builder()
                .applicationId(1l)
                .build();

        CreditDTO creditDTO = CreditDTO.builder()
                .amount(BigDecimal.valueOf(1000000))
                .term(6)
                .monthlyPayment(BigDecimal.valueOf(16101))
                .rate(BigDecimal.valueOf(5))
                .psk(BigDecimal.valueOf(1159272))
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .paymentSchedule(paymentSchedule)
                .build();
        //Todo mapper
        Credit credit1 = Credit.builder()
                .amount(BigDecimal.valueOf(1000000))
                .term(6)
                .monthlyPayment(BigDecimal.valueOf(16101))
                .rate(BigDecimal.valueOf(5))
                .psk(BigDecimal.valueOf(1159272))
                .insuranceEnable(true)
                .salaryClient(true)
                .paymentSchedule(paymentSchedule)
                .build();

        Credit credit = credit1;
        credit.setCreditStatus(CreditStatus.CALCULATED.name());

        when(applicationRepository.save(any()))
                .thenReturn(application);


        Credit creditSaved = creditService.createCredit(creditDTO, application);

        System.out.println(creditSaved);


        assertEquals(creditSaved.getPsk(), creditDTO.getPsk());
        assertEquals(creditSaved.getMonthlyPayment(), creditDTO.getMonthlyPayment());

        assertEquals(creditSaved.getPaymentSchedule().size(), 2);
        assertEquals(creditSaved.getPaymentSchedule().get(0).getTotalPayment(), BigDecimal.valueOf(16101));

        verify(applicationRepository, times(1)).save(any());
    }
}