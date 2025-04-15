package com.bankprototype.deal.kafka.producer;

import com.bankprototype.deal.kafka.dto.AuditAction;
import com.bankprototype.deal.kafka.dto.EmailMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditProducerImpl implements AuditProducer {

    @Value("${topic-name.audit-action}")
    private String topicName;

    private final KafkaTemplate<String, EmailMessageDTO> kafkaTemplate;

    @Override
    public void send(AuditAction auditAction) {
        log.info("[send] >> auditAction: {}", auditAction);

        Message<AuditAction> message = MessageBuilder
                .withPayload(auditAction)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .build();

        kafkaTemplate.send(message);

        log.info("[send] << done");
    }
}
