package com.bankprototype.dossier.service.impl;

import com.bankprototype.dossier.model.EmailContent;
import com.bankprototype.dossier.model.EmailInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext
@SpyBean(JavaMailSender.class)
class MailServiceImplTest {


    @Autowired
    private MailServiceImpl sendMailService;


    @Test
    void createEmailMimeMessage() throws MessagingException, IOException {

        EmailContent emailContent = new EmailContent(1L, "Theme1", "Text1");
        EmailInfo emailInfo =
                new EmailInfo("address1", emailContent);

        //when(sendMailService.createEmailMimeMessage(any())).thenReturn(any(MimeMessage.class));

        MimeMessage mimeMessage = sendMailService.createEmailMimeMessage(emailInfo);

        //MimeMessage emailCaptorValue = emailServiceCaptor.getValue();
        //EmailInfo emailInfo1 = emailServiceCaptorEmailInfo.getValue();
        System.out.println("javaMailSender want to send this MimeMessage:" + mimeMessage.getContent().toString());

        assertThat(mimeMessage.getSubject()).isEqualTo("Theme1");
        assertThat(mimeMessage.getAllRecipients()).hasSize(1);


    }
}