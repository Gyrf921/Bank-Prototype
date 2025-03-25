package org.bankprototype.audit.service;

import lombok.NonNull;
import org.bankprototype.audit.domein.entity.AuditActionEntity;
import org.bankprototype.audit.domein.model.AuditAction;

import java.util.List;
import java.util.UUID;

public interface AuditService {
    void saveAction(String auditActionMessage);

    AuditAction getAction(@NonNull UUID id);

    List<AuditAction> getActions();

}
