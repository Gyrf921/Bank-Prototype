package com.bankprototype.creditconveyor.service.impl;

import com.bankprototype.creditconveyor.exception.BadScoringInfoException;
import com.bankprototype.creditconveyor.web.dto.CreditDTO;
import com.bankprototype.creditconveyor.web.dto.EmploymentDTO;
import com.bankprototype.creditconveyor.web.dto.ScoringDataDTO;
import com.bankprototype.creditconveyor.web.dto.enamfordto.EmploymentStatus;
import com.bankprototype.creditconveyor.web.dto.enamfordto.Gender;
import com.bankprototype.creditconveyor.web.dto.enamfordto.MaritalStatus;
import com.bankprototype.creditconveyor.web.dto.enamfordto.Position;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class ScoringCalculationImplTest extends BaseServiceTest {


    @Autowired
    private ScoringCalculationImpl scoringCalculation;

    @Test
    void createCredit() {
        EmploymentDTO employmentDTO = EmploymentDTO.builder()
                .employmentStatus(EmploymentStatus.BUSINESS_OWNER)
                .position(Position.OWNER)
                .salary(BigDecimal.valueOf(400000))
                .workExperienceTotal(55)
                .workExperienceCurrent(44)
                .build();

        ScoringDataDTO scoringDataDTO = ScoringDataDTO.builder()
                .amount(BigDecimal.valueOf(1000000))
                .term(6)
                .firstName("testName")
                .lastName("testName")
                .middleName("testName")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1990, 7, 7))
                .passportSeries("1111")
                .passportNumber("222222")
                .passportIssueDate(LocalDate.of(2020, 7, 7))
                .passportIssueBranch("issue Branch")
                .maritalStatus(MaritalStatus.MARRIED)
                .dependentAmount(1)
                .employment(employmentDTO)
                .account("testAccount")
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();

        CreditDTO creditDTO = scoringCalculation.createCredit(scoringDataDTO);

        System.out.println(creditDTO);

        System.out.println("creditDTO.getPaymentSchedule().get(0).getInterestPayment() = " + creditDTO.getPaymentSchedule().get(0).getInterestPayment());
        System.out.println("creditDTO.getPaymentSchedule().get(10).getInterestPayment() = " + creditDTO.getPaymentSchedule().get(10).getInterestPayment());
        assertThat(creditDTO.getPaymentSchedule().get(0).getInterestPayment()).isNotEqualTo(creditDTO.getPaymentSchedule().get(10).getInterestPayment());


    }

    @Test
    void createCreditExceptionBirthdate() {
        EmploymentDTO employmentDTO = EmploymentDTO.builder()
                .employmentStatus(EmploymentStatus.BUSINESS_OWNER)
                .position(Position.OWNER)
                .salary(BigDecimal.valueOf(400000))
                .workExperienceTotal(55)
                .workExperienceCurrent(44)
                .build();

        ScoringDataDTO scoringDataDTO = ScoringDataDTO.builder()
                .amount(BigDecimal.valueOf(1000000))
                .term(6)
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(2020, 7, 7))
                .maritalStatus(MaritalStatus.SINGLE)
                .employment(employmentDTO)
                .dependentAmount(1)
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build();


        assertThrows(BadScoringInfoException.class, () -> {
            CreditDTO creditDTO = scoringCalculation.createCredit(scoringDataDTO);
            System.out.println(creditDTO);
        });

    }

    @Test
    void createCreditExceptionEmploymentStatus() {
        EmploymentDTO employmentDTO = EmploymentDTO.builder()
                .employmentStatus(EmploymentStatus.UNEMPLOYED)
                .position(Position.WORKER)
                .salary(BigDecimal.valueOf(400000))
                .workExperienceTotal(55)
                .workExperienceCurrent(44)
                .build();

        ScoringDataDTO scoringDataDTO = ScoringDataDTO.builder()
                .amount(BigDecimal.valueOf(1000000))
                .term(6)
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(2000, 7, 7))
                .maritalStatus(MaritalStatus.SINGLE)
                .employment(employmentDTO)
                .dependentAmount(1)
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build();


        assertThrows(BadScoringInfoException.class, () -> {
            CreditDTO creditDTO = scoringCalculation.createCredit(scoringDataDTO);
            System.out.println(creditDTO);
        });

    }

    @Test
    void createCreditExceptionSalary() {

        EmploymentDTO employmentDTO = EmploymentDTO.builder()
                .employmentStatus(EmploymentStatus.BUSINESS_OWNER)
                .position(Position.OWNER)
                .salary(BigDecimal.valueOf(1000))
                .workExperienceTotal(55)
                .workExperienceCurrent(44)
                .build();

        ScoringDataDTO scoringDataDTO = ScoringDataDTO.builder()
                .amount(BigDecimal.valueOf(1000000))
                .term(6)
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(2000, 7, 7))
                .maritalStatus(MaritalStatus.SINGLE)
                .employment(employmentDTO)
                .dependentAmount(1)
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build();

        assertThrows(BadScoringInfoException.class, () -> {
            CreditDTO creditDTO = scoringCalculation.createCredit(scoringDataDTO);
            System.out.println(creditDTO);
        });

    }

    @Test
    void createCreditExceptionTotalWorkExperience() {

        EmploymentDTO employmentDTO = EmploymentDTO.builder()
                .employmentStatus(EmploymentStatus.BUSINESS_OWNER)
                .position(Position.OWNER)
                .salary(BigDecimal.valueOf(400000))
                .workExperienceTotal(10)
                .workExperienceCurrent(44)
                .build();

        ScoringDataDTO scoringDataDTO = ScoringDataDTO.builder()
                .amount(BigDecimal.valueOf(1000000))
                .term(6)
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(2000, 7, 7))
                .maritalStatus(MaritalStatus.SINGLE)
                .employment(employmentDTO)
                .dependentAmount(1)
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build();


        assertThrows(BadScoringInfoException.class, () -> {
            CreditDTO creditDTO = scoringCalculation.createCredit(scoringDataDTO);
            System.out.println(creditDTO);
        });

    }
}