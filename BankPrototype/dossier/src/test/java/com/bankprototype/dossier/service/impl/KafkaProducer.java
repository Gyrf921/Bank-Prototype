package com.bankprototype.dossier.service.impl;

import com.bankprototype.dossier.kafka.dto.EmailMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@Component
@Slf4j
@ActiveProfiles("test")
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, EmailMessageDTO> kafkaTemplate;

    public void send(String topic, EmailMessageDTO payload) {
        kafkaTemplate.send(topic, payload);
    }
}