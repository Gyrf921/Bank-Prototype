package com.bankprototype.deal.mapper;

import com.bankprototype.deal.repository.dao.Credit;
import com.bankprototype.deal.web.dto.CreditDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CreditMapper {
    //CreditMapper INSTANCE = Mappers.getMapper(CreditMapper.class );

    @Mapping(target = "insuranceEnable", source = "creditDTO.isInsuranceEnabled")
    @Mapping(target = "salaryClient", source = "creditDTO.isSalaryClient")
    Credit creditDtoToCredit(CreditDTO creditDTO);


}
