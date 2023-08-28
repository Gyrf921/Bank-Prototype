package com.bankprototype.application.web.dto;

import com.bankprototype.application.web.dto.enamfordto.Gender;
import com.bankprototype.application.web.dto.enamfordto.MaritalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FinishRegistrationRequestDTO {

    @NotNull
    private Gender gender;

    @NotNull
    private MaritalStatus maritalStatus;

    @NotNull
    @DecimalMin(value = "10000")
    private BigDecimal dependentAmount;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate passportIssueDate;

    @NotNull
    private String passportIssueBranch;

    @NotNull
    private EmploymentDTO employment;

    @NotNull
    private String account;

}
