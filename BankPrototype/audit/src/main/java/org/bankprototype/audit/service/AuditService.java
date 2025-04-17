package org.bankprototype.audit.service;

import lombok.NonNull;
import org.bankprototype.audit.controller.dto.AuditActionRequestDto;
import org.bankprototype.audit.controller.dto.AuditActionResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public interface AuditService {

    void saveAction(String action);

    List<AuditActionResponseDto> getActions(AuditActionRequestDto auditActionDto, Pageable page);

    AuditActionResponseDto getAction(@NonNull UUID id);

    List<AuditActionResponseDto> getActions();

}
