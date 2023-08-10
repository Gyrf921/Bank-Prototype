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
    StatusHistoryMapper INSTANCE = Mappers.getMapper(StatusHistoryMapper.class );

    @Mapping(target = "time", source = "applicationStatusHistoryDTO.time", qualifiedByName = "localDateTimeToTimestamp")
    StatusHistory applicationStatusHistoryDtoToStatusHistory(ApplicationStatusHistoryDTO applicationStatusHistoryDTO);

    @Named("localDateTimeToTimestamp")
    static Timestamp localDateTimeToTimestamp(LocalDateTime time) {
        return Timestamp.valueOf(time);
    }
}

