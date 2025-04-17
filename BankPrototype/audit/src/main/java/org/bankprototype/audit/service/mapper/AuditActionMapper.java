package org.bankprototype.audit.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bankprototype.audit.controller.dto.AuditActionRequestDto;
import org.bankprototype.audit.controller.dto.AuditActionResponseDto;
import org.bankprototype.audit.entity.AuditActionEntity;
import org.bankprototype.audit.exception.BadKafkaMessageException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditActionMapper {
    private final ObjectMapper objectMapper;

    public AuditActionEntity map(AuditActionRequestDto action) {
        return objectMapper.convertValue(action, AuditActionEntity.class);
    }
    public AuditActionResponseDto map(AuditActionEntity entity) {
        return objectMapper.convertValue(entity, AuditActionResponseDto.class);
    }

    public AuditActionEntity parseStringToEntity(String json) {
        return map(parseFromString(json));
    }

    public AuditActionRequestDto parseFromString(String json) {
        AuditActionRequestDto action = null;
        try{
            log.info("Try to parse AuditAction from string {}", json);
            action = objectMapper.readValue(json, AuditActionRequestDto.class);
        }
        catch (JsonProcessingException e){
            throw new BadKafkaMessageException(e.getMessage());
        }
        return action;
    }

}
