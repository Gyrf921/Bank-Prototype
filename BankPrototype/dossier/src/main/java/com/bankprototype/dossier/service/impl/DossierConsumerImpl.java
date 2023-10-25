package com.bankprototype.dossier.service.impl;

import com.bankprototype.dossier.config.EmailPropertiesConfig;
import com.bankprototype.dossier.exception.BadKafkaMessageException;
import com.bankprototype.dossier.kafka.dto.EmailMessageDTO;
import com.bankprototype.dossier.model.EmailContent;
import com.bankprototype.dossier.model.EmailInfo;
import com.bankprototype.dossier.service.DossierConsumer;
import com.bankprototype.dossier.service.MailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class DossierConsumerImpl implements DossierConsumer {

    private final MailService mailService;

    private final ObjectMapper objectMapper;

    private final EmailPropertiesConfig emailConfig;

    @Override
    @KafkaListener(topics = "${topic-name.finish-registration}")
    public void consumeFinishRegistration(String message) {
        log.info("[consumeFinishRegistration] >> massage: {}", message);

        EmailInfo emailInfo = prepareEmailInfoForSend(message, emailConfig.getCreditIssuedTheme(), emailConfig.getCreditIssuedText());

        MimeMessage email = mailService.createEmailMimeMessage(emailInfo);

        mailService.sendEmail(email);

        log.info("[consumeFinishRegistration] << result void");
    }

    @Override
    @KafkaListener(topics = "${topic-name.create-documents}")
    public void consumeCreateDocuments(String message) {
        log.info("[consumeCreateDocuments] >> message: {}", message);

        EmailInfo emailInfo = prepareEmailInfoForSend(message, emailConfig.getCreditIssuedTheme(), emailConfig.getCreditIssuedText());

        MimeMessage email = mailService.createEmailMimeMessage(emailInfo);

        mailService.sendEmail(email);

        log.info("[consumeCreateDocuments] << result void");
    }

    @Override
    @KafkaListener(topics = "${topic-name.send-documents}")
    public void consumeSendDocuments(String message) {
        log.info("[consumeSendDocuments] >> message: {}", message);

        EmailInfo emailInfo = prepareEmailInfoForSend(message, emailConfig.getCreditIssuedTheme(), emailConfig.getCreditIssuedText());

        MimeMessage email = mailService.createEmailMimeMessage(emailInfo);

        mailService.sendEmail(email);

        log.info("[consumeSendDocuments] << result void");
    }

    @Override
    @KafkaListener(topics = "${topic-name.send-ses}")
    public void consumeSendSes(String message) {
        log.info("[consumeSendSes] >> message: {}", message);

        EmailInfo emailInfo = prepareEmailInfoForSend(message, emailConfig.getCreditIssuedTheme(), emailConfig.getCreditIssuedText());

        MimeMessage email = mailService.createEmailMimeMessage(emailInfo);

        mailService.sendEmail(email);

        log.info("[consumeSendSes] << result void");
    }

    @Override
    @KafkaListener(topics = "${topic-name.credit-issued}")
    public void consumeCreditIssued(String message) {
        log.info("[consumeCreditIssued] >> message: {}", message);

        EmailInfo emailInfo = prepareEmailInfoForSend(message, emailConfig.getCreditIssuedTheme(), emailConfig.getCreditIssuedText());

        MimeMessage email = mailService.createEmailMimeMessage(emailInfo);

        mailService.sendEmail(email);

        log.info("[consumeCreditIssued] << result void");
    }

    @Override
    @KafkaListener(topics = "${topic-name.application-denied}")
    public void consumeApplicationDenied(String message) {
        log.info("[consumeApplicationDenied] >> message: {}", message);

        EmailInfo emailInfo = prepareEmailInfoForSend(message, emailConfig.getCreditIssuedTheme(), emailConfig.getCreditIssuedText());

        MimeMessage email = mailService.createEmailMimeMessage(emailInfo);

        mailService.sendEmail(email);

        log.info("[consumeApplicationDenied] << result void");
    }

    private EmailInfo prepareEmailInfoForSend(String messageFromKafka, String theme, String text) {
        log.info("[sendEmailFromTheKafkaMessage] >> messageFromKafka: {}, theme: {}", messageFromKafka, theme);

        EmailMessageDTO emailMessageDTO = converteDataStringToEmailMessageDTO(messageFromKafka);

        EmailContent emailContent = prepareEmailContent(emailMessageDTO, theme, text);

        EmailInfo emailInfo = createEmailInfo(emailMessageDTO.getAddress(), emailContent);

        log.info("[sendEmailWithSesCode] << result void, email: {}", emailInfo);
        return emailInfo;
    }

    private EmailInfo createEmailInfo(String address, EmailContent emailContent) {
        log.info("[createEmailInfo] >> address: {}, emailContent: {}", address, emailContent);
        return new EmailInfo(address, emailContent);
    }


    private EmailMessageDTO converteDataStringToEmailMessageDTO(String emailMessageInString) {

        EmailMessageDTO email;
        try {
            email = objectMapper.readValue(emailMessageInString, EmailMessageDTO.class);
        } catch (JsonProcessingException e) {
            log.error("Error converting kafka message to EmailMessageDTO");
            throw new BadKafkaMessageException("Error converting kafka message to EmailMessageDTO, exception's message: " + e.getMessage());
        }

        return email;
    }

    private EmailContent prepareEmailContent(EmailMessageDTO emailMessageDTO, String theme, String text) {

        if (emailMessageDTO.getSesCode() != null) {
            return new EmailContent(emailMessageDTO.getApplicationId(), theme, text.concat(emailMessageDTO.getSesCode().toString()));
        }
        return new EmailContent(emailMessageDTO.getApplicationId(), theme, text);
    }

}
