package com.bankprototype.deal.service.impl;

import com.bankprototype.deal.repository.dao.Application;
import com.bankprototype.deal.repository.dao.Client;
import com.bankprototype.deal.repository.dao.Credit;
import com.bankprototype.deal.repository.dao.enumfordao.CreditStatus;
import com.bankprototype.deal.repository.dao.enumfordao.EmploymentStatus;
import com.bankprototype.deal.repository.dao.enumfordao.Gender;
import com.bankprototype.deal.repository.dao.jsonb.Passport;
import com.bankprototype.deal.mapper.CreditMapper;
import com.bankprototype.deal.repository.CreditRepository;
import com.bankprototype.deal.web.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditServiceImplTest {

    @Mock
    private CreditRepository creditRepository;

    @InjectMocks
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
        assertEquals(scoringDataDTO.getIsInsuranceEnabled(), true);
        assertEquals(scoringDataDTO.getIsSalaryClient(), true);

    }

    @Test
    void createCredit() {

        PaymentScheduleElement payment1 = PaymentScheduleElement.builder()
                .totalPayment(BigDecimal.valueOf(16101)).build();
        PaymentScheduleElement payment2 = PaymentScheduleElement.builder()
                .totalPayment(BigDecimal.valueOf(16101)).build();
        List<PaymentScheduleElement> paymentSchedule = List.of(payment1, payment2);

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

        Credit credit = CreditMapper.INSTANCE.creditDtoToCredit(creditDTO);
        credit.setCreditStatus(CreditStatus.CALCULATED.name());

        when(creditRepository.save(any()))
                .thenReturn(credit);

        Credit creditSaved = creditService.createCredit(creditDTO, application);

        System.out.println(creditSaved);


        assertEquals(creditSaved.getPsk(), creditDTO.getPsk());
        assertEquals(creditSaved.getMonthlyPayment(), creditDTO.getMonthlyPayment());

        assertEquals(creditSaved.getPaymentSchedule().size(), 2);
        assertEquals(creditSaved.getPaymentSchedule().get(0).getTotalPayment(), BigDecimal.valueOf(16101));

        verify(creditRepository, times(1)).save(any());
    }
}