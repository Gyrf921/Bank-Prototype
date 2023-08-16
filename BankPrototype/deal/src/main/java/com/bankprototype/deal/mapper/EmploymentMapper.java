package com.bankprototype.deal.mapper;

import com.bankprototype.deal.repository.dao.jsonb.Employment;
import com.bankprototype.deal.web.dto.EmploymentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmploymentMapper {

    @Mapping(target = "employmentStatus", source = "employment.status")
    @Mapping(target = "employerINN", source = "employment.employerInn")
    EmploymentDTO employmentToEmploymentDto(Employment employment);

    @Mapping(source = "employmentDTO.employmentStatus", target = "status")
    @Mapping(source = "employmentDTO.employerINN", target = "employerInn")
    Employment employmentDtoToEmployment(EmploymentDTO employmentDTO);
}
