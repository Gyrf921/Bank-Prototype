package com.bankprototype.deal.mapper;

import com.bankprototype.deal.repository.dao.jsonb.StatusHistory;
import com.bankprototype.deal.web.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Mapper
public interface StatusHistoryMapper {

    @Mapping(target = "time", source = "applicationStatusHistoryDTO.time")
    StatusHistory applicationStatusHistoryDtoToStatusHistory(ApplicationStatusHistoryDTO applicationStatusHistoryDTO);

}

