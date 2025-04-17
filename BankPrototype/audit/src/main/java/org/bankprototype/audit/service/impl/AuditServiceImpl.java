package org.bankprototype.audit.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bankprototype.audit.controller.dto.AuditActionRequestDto;
import org.bankprototype.audit.controller.dto.AuditActionResponseDto;
import org.bankprototype.audit.entity.AuditActionEntity;
import org.bankprototype.audit.repository.AuditActionRepository;
import org.bankprototype.audit.service.AuditService;
import org.bankprototype.audit.service.mapper.AuditActionMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditServiceImpl implements AuditService {

    private final AuditActionMapper auditActionMapper;
    private final AuditActionRepository actionRepository;

    @Override
    public void saveAction(String action) {
        log.info("Saving action");
        actionRepository.save(auditActionMapper.parseStringToEntity(action));
    }

    @Override
    public List<AuditActionResponseDto> getActions(AuditActionRequestDto auditActionDto, Pageable page) {
        AuditActionEntity example = auditActionMapper.map(auditActionDto);
        return actionRepository.findAll(Example.of(example), page)
                .get().map(auditActionMapper::map).toList();
    }
    @Override
    public AuditActionResponseDto getAction(@NonNull UUID id) {
        return actionRepository.findById(id)
                .map(auditActionMapper::map)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Action found for id %s", id)));
    }

    @Override
    public List<AuditActionResponseDto> getActions() {
        List<AuditActionResponseDto> actions = new ArrayList<>();
        actionRepository.findAll().forEach(action -> actions.add(auditActionMapper.map(action)));
        return actions;
    }
}
