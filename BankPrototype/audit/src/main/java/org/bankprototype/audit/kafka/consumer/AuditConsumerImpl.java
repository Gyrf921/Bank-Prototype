package org.bankprototype.audit.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bankprototype.audit.service.AuditService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditConsumerImpl {

    private final AuditService auditService;

    @KafkaListener(topics = "${topic-name.audit-action}")
    public void consumeAuditMessage(String auditAction) {
        log.info("[consumeAuditMessage] >> take auditAction message");
        if (auditAction != null){
            auditService.saveAction(auditAction);
        }
        log.info("[consumeAuditMessage] << done");
    }
}
