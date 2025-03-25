package org.bankprototype.audit.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bankprototype.audit.domein.entity.AuditActionEntity;
import org.bankprototype.audit.domein.mapper.AuditActionMapper;
import org.bankprototype.audit.domein.model.AuditAction;
import org.bankprototype.audit.repository.AuditActionRepository;
import org.bankprototype.audit.service.AuditService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditServiceImpl implements AuditService {

    private final AuditActionMapper actionMapper;
    private final AuditActionRepository actionRepository;

    @Override
    public void saveAction(String auditActionMessage) {
        log.info("Saving action");
        actionRepository.save(actionMapper.parseStringToEntity(auditActionMessage));
    }

    @Override
    public AuditAction getAction(@NonNull UUID id) {
        return actionRepository.findById(id)
                .map(actionMapper::map)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Action found for id %s", id)));
    }

    @Override
    public List<AuditAction> getActions() {
        List<AuditAction> actions = new ArrayList<>();
        actionRepository.findAll().forEach(action -> actions.add(actionMapper.map(action)));
        return actions;
    }
}
