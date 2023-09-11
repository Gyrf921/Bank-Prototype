package com.bankprototype.gateway.web.dto;

import com.bankprototype.gateway.web.dto.enumfordto.Gender;
import com.bankprototype.gateway.web.dto.enumfordto.MaritalStatus;
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
    private String passportIssueBrach;

    @NotNull
    private EmploymentDTO employment;

    @NotNull
    private String account;

}
