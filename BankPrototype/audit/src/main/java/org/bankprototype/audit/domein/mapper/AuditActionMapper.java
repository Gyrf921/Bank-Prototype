package org.bankprototype.audit.domein.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bankprototype.audit.domein.entity.AuditActionEntity;
import org.bankprototype.audit.domein.model.AuditAction;
import org.bankprototype.audit.exception.BadKafkaMessageException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditActionMapper {
    private final ObjectMapper objectMapper;

    public AuditActionEntity map(AuditAction action) {
        return objectMapper.convertValue(action, AuditActionEntity.class);
    }
    public AuditAction map(AuditActionEntity entity) {
        return objectMapper.convertValue(entity, AuditAction.class);
    }

    public AuditActionEntity parseStringToEntity(String json) {
        return map(parseFromString(json));
    }

    public AuditAction parseFromString(String json) {
        AuditAction action = null;
        try{
            log.info("Try to parse AuditAction from string {}", json);
            action = objectMapper.readValue(json, AuditAction.class);
        }
        catch (JsonProcessingException e){
            throw new BadKafkaMessageException(e.getMessage());
        }
        return action;
    }

}
