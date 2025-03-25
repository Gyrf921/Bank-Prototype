package org.bankprototype.audit.repository;

import org.bankprototype.audit.domein.entity.AuditActionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuditActionRepository extends CrudRepository<AuditActionEntity, UUID> {
}
