package com.bankprototype.deal.service.impl;


import com.bankprototype.deal.kafka.EmailMessageDTO;
import com.bankprototype.deal.kafka.enumfordto.Theme;
import com.bankprototype.deal.repository.dao.Application;
import com.bankprototype.deal.service.ApplicationService;
import com.bankprototype.deal.service.DealProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealProducerImpl implements DealProducer {

    private final ApplicationService applicationService;

    private final KafkaTemplate<String, EmailMessageDTO> kafkaTemplate;


    @Override
    public EmailMessageDTO createMessage(Long applicationId, Theme theme) {
        log.info("[createMessage] >> applicationId: {}, theme: {}", applicationId, theme);

        Application application = applicationService.getApplicationById(applicationId);

        EmailMessageDTO massageDTO = new EmailMessageDTO(application.getClientId().getEmail(), theme, applicationId);

        log.info("[createMessage] << result: {}", massageDTO);

        return massageDTO;
    }

    @Override
    public void sendMessage(EmailMessageDTO massageDTO, String topicName) {
        log.info("[sendMessage] >> massageDTO: {}", massageDTO);

        Message<EmailMessageDTO> message = MessageBuilder
                .withPayload(massageDTO)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .build();

        kafkaTemplate.send(message);

        log.info("[sendMessage] << result is void.");
    }


}
