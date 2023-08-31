package com.bankprototype.deal.service.impl;


import com.bankprototype.deal.service.DealProducer;
import com.bankprototype.deal.web.dto.EmailMassageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealProducerImpl implements DealProducer {

    private final KafkaTemplate<String, EmailMassageDTO> kafkaTemplate;

    @Override
    public void sendMessage(EmailMassageDTO massageDTO, String topicName) {
        log.info("[sendMessage] >> massageDTO: {}", massageDTO);

        Message<EmailMassageDTO> message = MessageBuilder
                .withPayload(massageDTO)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .build();

        kafkaTemplate.send(message);

        log.info("[sendMessage] << result is void.");
    }
}
