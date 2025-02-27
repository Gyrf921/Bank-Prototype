package com.bankprototype.deal.dao.mapper;

import com.bankprototype.deal.dao.jsonb.StatusHistory;
import com.bankprototype.deal.web.dto.ApplicationStatusHistoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StatusHistoryMapper {

    @Mapping(target = "time", source = "applicationStatusHistoryDTO.time")
    StatusHistory map(ApplicationStatusHistoryDTO applicationStatusHistoryDTO);

}

