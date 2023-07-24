package com.bankprototype.creditconveyor.web.dto;

import com.bankprototype.creditconveyor.web.dto.enam_for_dto.Gender;
import com.bankprototype.creditconveyor.web.dto.enam_for_dto.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ScoringDataDTO {
    private BigDecimal amount;
    private Integer term;
    private String firstName;
    private String lastName;
    private String middleName;
    private Gender gender;
    private LocalDate birthdate;
    private String passportSeries;
    private String passportNumber;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private MaritalStatus maritalStatus;
    private Integer dependentAmount;
    private EmploymentDTO employment;
    private String account;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
}
