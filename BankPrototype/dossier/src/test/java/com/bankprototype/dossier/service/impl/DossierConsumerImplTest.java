package com.bankprototype.dossier.service.impl;

import com.bankprototype.dossier.kafka.dto.EmailMassageDTO;
import com.bankprototype.dossier.kafka.dto.enamfordto.Theme;
import com.bankprototype.dossier.service.SendMailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
    private SendMailService mailService;

    @Captor
    ArgumentCaptor<EmailMassageDTO> emailKafkaCaptor;

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
        EmailMassageDTO message = new EmailMassageDTO("gyrf921@gmail.com", Theme.FINISH_REGISTRATION, 1L);

        testKafkaConsumer(finishRegistration, message);
    }

    @Test
    void testKafkaConsumer_createDocuments() {
        EmailMassageDTO message = new EmailMassageDTO("gyrf921@gmail.com", Theme.CREATE_DOCUMENTS, 1L);

        testKafkaConsumer(createDocuments, message);
    }

    @Test
    void testKafkaConsumer_sendDocuments() {
        EmailMassageDTO message = new EmailMassageDTO("gyrf921@gmail.com", Theme.SEND_DOCUMENTS, 1L);

        testKafkaConsumer(sendDocuments, message);
    }

    @Test
    void testKafkaConsumer_creditIssued() {
        EmailMassageDTO message = new EmailMassageDTO("gyrf921@gmail.com", Theme.CREDIT_ISSUED, 1L);

        testKafkaConsumer(creditIssued, message);
    }

    @Test
    void testKafkaConsumer_sendSes() {
        EmailMassageDTO message = new EmailMassageDTO("gyrf921@gmail.com", Theme.SEND_SES, 1L);

        testKafkaConsumer(sendSes, message);
    }

    @Test
    void testKafkaConsumer_applicationDenied() {
        EmailMassageDTO message = new EmailMassageDTO("gyrf921@gmail.com", Theme.APPLICATION_DENIED, 1L);

        testKafkaConsumer(applicationDenied, message);
    }


    void testKafkaConsumer(String topicName, EmailMassageDTO message) {

        kafkaProducer.send(topicName, message);

        doNothing().when(mailService).sendEmail(any(), any(), any());

        verify(mailService, timeout(5000)).sendEmail(any(), any(), any());

        verify(mailService).sendEmail(emailKafkaCaptor.capture(), any(), any());
        EmailMassageDTO emailCaptorValue = emailKafkaCaptor.getValue();
        System.out.println("SendMailService received this EmailMassageDTO:" + emailCaptorValue);
    }

}
