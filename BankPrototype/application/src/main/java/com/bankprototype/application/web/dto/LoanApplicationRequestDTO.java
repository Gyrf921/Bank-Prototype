package com.bankprototype.application.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoanApplicationRequestDTO {

    @NotNull
    @DecimalMin(value = "10000")
    private BigDecimal amount;

    @NotNull
    @Min(value = 6)
    private Integer term;

    @NotNull
    @Length(min = 2, max = 30, message = "firstName length must be from 2 to 30")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "firstName must contain only letters a-z and A-Z")
    private String firstName;

    @NotNull
    @Length(min = 2, max = 30, message = "lastName length must be from 2 to 30")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "lastName must contain only letters a-z and A-Z")
    private String lastName;

    @Length(min = 2, max = 30, message = "middleName length must be from 2 to 30")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "middleName must contain only letters a-z and A-Z")
    private String middleName;

    @NotNull
    @Email
    private String email;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Past(message = "birthday can't be after current date")
    private LocalDate birthDate;

    @NotNull
    @Length(min = 4, max = 4, message = "passportSeries it is 4 number")
    @Pattern(regexp = "^[0-9]+$", message = "passportSeries it is 4 number")
    private String passportSeries;

    @NotNull
    @Length(min = 6, max = 6, message = "passportNumber it is 6 number")
    @Pattern(regexp = "^[0-9]+$", message = "passportNumber it is 6 number")
    private String passportNumber;
}
