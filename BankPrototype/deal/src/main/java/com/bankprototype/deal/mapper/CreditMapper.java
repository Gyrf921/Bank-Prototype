package com.bankprototype.deal.mapper;

import com.bankprototype.deal.repository.dao.Credit;
import com.bankprototype.deal.web.dto.CreditDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CreditMapper {

    @Mapping(target = "insuranceEnable", source = "creditDTO.isInsuranceEnabled")
    @Mapping(target = "salaryClient", source = "creditDTO.isSalaryClient")
    Credit creditDtoToCredit(CreditDTO creditDTO);


}
