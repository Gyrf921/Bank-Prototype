package com.bankprototype.creditconveyor.web.dto;

import com.bankprototype.creditconveyor.web.dto.enam_for_dto.EmploymentStatus;
import com.bankprototype.creditconveyor.web.dto.enam_for_dto.Position;

import java.math.BigDecimal;

public class EmploymentDTO {

    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    private Position position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
