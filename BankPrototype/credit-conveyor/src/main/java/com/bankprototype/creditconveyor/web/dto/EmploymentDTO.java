package com.bankprototype.creditconveyor.web.dto;

import com.bankprototype.creditconveyor.web.dto.enamfordto.EmploymentStatus;
import com.bankprototype.creditconveyor.web.dto.enamfordto.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmploymentDTO {

    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    private Position position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
