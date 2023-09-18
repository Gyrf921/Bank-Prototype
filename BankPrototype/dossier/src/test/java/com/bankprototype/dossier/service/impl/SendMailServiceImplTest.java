package com.bankprototype.dossier.service.impl;

import com.bankprototype.dossier.kafka.dto.EmailMassageDTO;
import com.bankprototype.dossier.kafka.dto.enamfordto.Theme;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.annotation.DirtiesContext;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;


@SpringBootTest
@DirtiesContext
@SpyBean(JavaMailSender.class)
class SendMailServiceImplTest {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SendMailServiceImpl sendMailService;
    @Captor
    ArgumentCaptor<MimeMessage> emailServiceCaptor;

    @Test
    void shouldUseGreenMail() throws MessagingException, IOException {
        String testTheme = "theme";
        String testEmailAddress = "gyrf921@gmail.com";
        EmailMassageDTO emailMassageDTO = new EmailMassageDTO(testEmailAddress, Theme.SEND_SES, 1L, null);

        doNothing().when(javaMailSender).send(any(MimeMessage.class));

        sendMailService.sendEmail(emailMassageDTO, testTheme, "test text for email sender");


        verify(javaMailSender, times(1)).send(emailServiceCaptor.capture());
        MimeMessage emailCaptorValue = emailServiceCaptor.getValue();
        System.out.println("javaMailSender want to send this MimeMessage:" + emailCaptorValue.getContent().toString());

        assertThat(emailCaptorValue.getSubject()).isEqualTo(testTheme);
        assertThat(emailCaptorValue.getAllRecipients()).hasSize(1);

    }
}