package com.bankprototype.deal.kafka.producer;

import com.bankprototype.deal.kafka.dto.AuditAction;

public interface AuditProducer {

    void send(AuditAction auditAction);
}
