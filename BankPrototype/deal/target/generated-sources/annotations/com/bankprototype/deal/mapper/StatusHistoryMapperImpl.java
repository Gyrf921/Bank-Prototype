package com.bankprototype.deal.mapper;

import com.bankprototype.deal.repository.dao.jsonb.StatusHistory;
import com.bankprototype.deal.web.dto.ApplicationStatusHistoryDTO;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-15T14:06:49+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
public class StatusHistoryMapperImpl implements StatusHistoryMapper {

    @Override
    public StatusHistory applicationStatusHistoryDtoToStatusHistory(ApplicationStatusHistoryDTO applicationStatusHistoryDTO) {
        if ( applicationStatusHistoryDTO == null ) {
            return null;
        }

        StatusHistory.StatusHistoryBuilder statusHistory = StatusHistory.builder();

        statusHistory.time( StatusHistoryMapper.localDateTimeToTimestamp( applicationStatusHistoryDTO.getTime() ) );
        statusHistory.status( applicationStatusHistoryDTO.getStatus() );
        statusHistory.changeType( applicationStatusHistoryDTO.getChangeType() );

        return statusHistory.build();
    }
}
