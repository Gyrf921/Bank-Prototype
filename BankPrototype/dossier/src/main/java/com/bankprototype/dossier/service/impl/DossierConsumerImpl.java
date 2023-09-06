package com.bankprototype.dossier.service.impl;

import com.bankprototype.dossier.config.EmailPropertiesConfig;
import com.bankprototype.dossier.exception.BadKafkaMessageException;
import com.bankprototype.dossier.kafka.dto.EmailMassageDTO;
import com.bankprototype.dossier.service.DossierConsumer;
import com.bankprototype.dossier.service.SendMailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DossierConsumerImpl implements DossierConsumer {

    private final SendMailService mailService;

    private final ObjectMapper objectMapper;

    private final EmailPropertiesConfig emailConfig;

    @Override
    @KafkaListener(topics = "${topic-name.finish-registration}")
    public void consumeFinishRegistration(String message) {
        log.info("[consumeFinishRegistration] >> massage: {}", message);

        sendEmailFromTheKafkaMessage(message, emailConfig.getFinishRegistrationTheme(), emailConfig.getFinishRegistrationText());

        log.info("[consumeFinishRegistration] << result void");
    }

    @Override
    @KafkaListener(topics = "${topic-name.create-documents}")
    public void consumeCreateDocuments(String message) {
        log.info("[consumeCreateDocuments] >> message: {}", message);

        sendEmailFromTheKafkaMessage(message, emailConfig.getCreateDocumentsTheme(), emailConfig.getCreateDocumentsText());

        log.info("[consumeCreateDocuments] << result void");
    }

    @Override
    @KafkaListener(topics = "${topic-name.send-documents}")
    public void consumeSendDocuments(String message) {
        log.info("[consumeSendDocuments] >> message: {}", message);

        sendEmailFromTheKafkaMessage(message, emailConfig.getSendDocumentsTheme(), emailConfig.getSendDocumentsText());

        log.info("[consumeSendDocuments] << result void");
    }

    @Override
    @KafkaListener(topics = "${topic-name.credit-issued}")
    public void consumeCreditIssued(String message) {
        log.info("[consumeCreditIssued] >> message: {}", message);

        sendEmailFromTheKafkaMessage(message, emailConfig.getCreditIssuedTheme(), emailConfig.getCreditIssuedText());

        log.info("[consumeCreditIssued] << result void");
    }

    @Override
    @KafkaListener(topics = "${topic-name.send-ses}")
    public void consumeSendSes(String message) {
        log.info("[consumeSendSes] >> message: {}", message);

        sendEmailFromTheKafkaMessage(message, emailConfig.getSendSesTheme(), emailConfig.getSendSesText());
        log.info("[consumeSendSes] << result void");
    }

    @Override
    @KafkaListener(topics = "${topic-name.application-denied}")
    public void consumeApplicationDenied(String message) {
        log.info("[consumeApplicationDenied] >> message: {}", message);

        sendEmailFromTheKafkaMessage(message, emailConfig.getApplicationDeniedTheme(), emailConfig.getApplicationDeniedText());

        log.info("[consumeApplicationDenied] << result void");
    }

    private void sendEmailFromTheKafkaMessage(String message, String theme, String text) {
        log.info("[sendEmailFromTheKafkaMessage] >> message: {}", message);

        EmailMassageDTO email;
        try {
            email = objectMapper.readValue(message, EmailMassageDTO.class);
        } catch (JsonProcessingException e) {
            log.error("Error converting kafka message to EmailMessageDTO");
            throw new BadKafkaMessageException("Error converting kafka message to EmailMessageDTO, exception's message: " + e.getMessage());
        }

        mailService.sendEmail(email, theme, text);

        log.info("[sendEmailFromTheKafkaMessage] << result void, email: {}", email);
    }

}
