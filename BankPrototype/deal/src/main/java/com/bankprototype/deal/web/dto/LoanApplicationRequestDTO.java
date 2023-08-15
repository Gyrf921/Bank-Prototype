package com.bankprototype.deal.web.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoanApplicationRequestDTO {

    @NotNull
    @DecimalMin(value = "10000")
    public BigDecimal amount;

    @NotNull
    @Min(value = 6)
    public Integer term;

    @NotNull
    @Length(min = 2, max = 30, message = "firstName length must be from 2 to 30")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "firstName must contain only letters a-z and A-Z")
    public String firstName;

    @NotNull
    @Length(min = 2, max = 30, message = "lastName length must be from 2 to 30")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "lastName must contain only letters a-z and A-Z")
    public String lastName;

    @Length(min = 2, max = 30, message = "middleName length must be from 2 to 30")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "middleName must contain only letters a-z and A-Z")
    public String middleName;

    @NotNull
    @Email
    public String email;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Past(message = "birthday can't be after current date")
    public LocalDate birthdate;

    @NotNull
    @Length(min = 4, max = 4, message = "passportSeries it is 4 number")
    @Pattern(regexp = "^[0-9]+$", message = "passportSeries it is 4 number")
    public String passportSeries;

    @NotNull
    @Length(min = 6, max = 6, message = "passportNumber it is 6 number")
    @Pattern(regexp = "^[0-9]+$", message = "passportNumber it is 6 number")
    public String passportNumber;
}
