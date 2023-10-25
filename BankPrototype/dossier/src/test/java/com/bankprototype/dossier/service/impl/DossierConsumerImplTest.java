package com.bankprototype.dossier.service.impl;

import com.bankprototype.dossier.kafka.dto.EmailMessageDTO;
import com.bankprototype.dossier.kafka.dto.enamfordto.Theme;
import com.bankprototype.dossier.model.EmailInfo;
import com.bankprototype.dossier.service.MailService;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.mail.internet.MimeMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"}, bootstrapServersProperty = "spring.kafka.bootstrap-servers")
class DossierConsumerImplTest {

    @Autowired
    private KafkaProducer kafkaProducer;

    @MockBean
    private MailService mailService;

    @Captor
    ArgumentCaptor<EmailInfo> emailKafkaCaptor;

    //region Topics
    @Value("${topic-name.finish-registration}")
    private String finishRegistration;
    @Value("${topic-name.create-documents}")
    private String createDocuments;
    @Value("${topic-name.send-documents}")
    private String sendDocuments;
    @Value("${topic-name.credit-issued}")
    private String creditIssued;
    @Value("${topic-name.send-ses}")
    private String sendSes;
    @Value("${topic-name.application-denied}")
    private String applicationDenied;
    //endregion

    @Test
    void testKafkaConsumer_finishRegistration() {
        EmailMessageDTO message = new EmailMessageDTO("gyrf921@gmail.com", Theme.FINISH_REGISTRATION, 1L, null);

        testKafkaConsumer(finishRegistration, message);
    }

    @Test
    void testKafkaConsumer_createDocuments() {
        EmailMessageDTO message = new EmailMessageDTO("gyrf921@gmail.com", Theme.FINISH_REGISTRATION, 1L, null);

        testKafkaConsumer(createDocuments, message);
    }

    @Test
    void testKafkaConsumer_sendDocuments() {
        EmailMessageDTO message = new EmailMessageDTO("gyrf921@gmail.com", Theme.FINISH_REGISTRATION, 1L, null);

        testKafkaConsumer(sendDocuments, message);
    }

    @Test
    void testKafkaConsumer_creditIssued() {
        EmailMessageDTO message = new EmailMessageDTO("gyrf921@gmail.com", Theme.FINISH_REGISTRATION, 1L, null);

        testKafkaConsumer(creditIssued, message);
    }

    @Test
    void testKafkaConsumer_sendSes() {
        EmailMessageDTO message = new EmailMessageDTO("gyrf921@gmail.com", Theme.FINISH_REGISTRATION, 1L, 666666L);

        testKafkaConsumer(sendSes, message);
    }

    @Test
    void testKafkaConsumer_applicationDenied() {
        EmailMessageDTO message = new EmailMessageDTO("gyrf921@gmail.com", Theme.APPLICATION_DENIED, 1L, null);

        testKafkaConsumer(applicationDenied, message);
    }

    void testKafkaConsumer(String topicName, EmailMessageDTO message) {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();

        kafkaProducer.send(topicName, message);

        when(mailService.createEmailMimeMessage(any()))
                .thenReturn(null);
        doNothing().when(mailService)
                .sendEmail(any(MimeMessage.class));

        verify(mailService, timeout(5000)).sendEmail(any());

        verify(mailService).createEmailMimeMessage(emailKafkaCaptor.capture());
        EmailInfo emailCaptorValue = emailKafkaCaptor.getValue();
        System.out.println("SendMailService received this EmailMessageDTO:" + emailCaptorValue);
    }

}
