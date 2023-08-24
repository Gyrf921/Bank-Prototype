package com.bankprototype.deal.service.impl;

import com.bankprototype.deal.mapper.CreditMapper;
import com.bankprototype.deal.repository.ApplicationRepository;
import com.bankprototype.deal.repository.dao.Application;
import com.bankprototype.deal.repository.dao.Client;
import com.bankprototype.deal.repository.dao.Credit;
import com.bankprototype.deal.repository.dao.enumfordao.CreditStatus;
import com.bankprototype.deal.repository.dao.enumfordao.EmploymentStatus;
import com.bankprototype.deal.repository.dao.enumfordao.Gender;
import com.bankprototype.deal.repository.dao.jsonb.Passport;
import com.bankprototype.deal.web.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CreditServiceImplTest {

    @MockBean
    private ApplicationRepository applicationRepository;

    @Autowired
    private CreditMapper creditMapper;

    @Autowired
    private CreditServiceImpl creditService;


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
        assertEquals(true, scoringDataDTO.getIsInsuranceEnabled());
        assertEquals(true, scoringDataDTO.getIsSalaryClient());

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
                .applicationId(1L)
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

        Credit credit = creditMapper.creditDtoToCredit(creditDTO);
        credit.setCreditStatus(CreditStatus.CALCULATED);

        when(applicationRepository.save(any()))
                .thenReturn(application);


        Credit creditSaved = creditService.createCredit(creditDTO, application);

        System.out.println(creditSaved);

        assertEquals(creditSaved.getPsk(), creditDTO.getPsk());
        assertEquals(creditSaved.getMonthlyPayment(), creditDTO.getMonthlyPayment());

        assertEquals(2, creditSaved.getPaymentSchedule().size());
        assertEquals(BigDecimal.valueOf(16101), creditSaved.getPaymentSchedule().get(0).getTotalPayment());

        verify(applicationRepository, times(1)).save(any());
    }
}