package com.bankprototype.dossier.service.impl;

import com.bankprototype.dossier.exception.CreateMimeMessageException;
import com.bankprototype.dossier.kafka.dto.EmailMassageDTO;
import com.bankprototype.dossier.service.SendMailService;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendMailServiceImpl implements SendMailService {

    private final JavaMailSender emailSender;

    private final Configuration configurationEmail;

    @Value("${spring.mail.username}")
    private String emailFrom;


    @Override
    public void sendEmail(EmailMassageDTO messageDTO, String theme, String text) {
        log.info("[sendEmail] >> messageDTO: {}, theme: {}, text: {}...", messageDTO, theme, text.substring(0, 10));

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            helper.setFrom(emailFrom);
            helper.setTo(messageDTO.getAddress());
            helper.setSubject(theme);

            String emailContent = getEmailContent(messageDTO, theme, text);
            helper.setText(emailContent, true);

        } catch (MessagingException | TemplateException | IOException e) {
            log.error("Error when creating a email");
            throw new CreateMimeMessageException("Error when creating a email, exception's message: " + e.getMessage());
        }

        emailSender.send(mimeMessage);

        log.info("[sendEmail] << result void, email: {}", mimeMessage);
    }

    private String getEmailContent(EmailMassageDTO emailMassageDTO, String theme, String textForUser) throws IOException, TemplateException {
        log.info("[sendEmail] >> emailMassageDTO: {}, theme: {}, textForUser: {}...", emailMassageDTO, theme, textForUser.substring(0, 10));

        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();

        model.put("emailMassageDTO", emailMassageDTO);
        model.put("theme", theme);
        model.put("textForUser", textForUser);

        configurationEmail.getTemplate("email.ftlh").process(model, stringWriter);

        String emailContent = stringWriter.getBuffer().toString();

        log.info("[sendEmail] << result: {}", emailContent);

        return emailContent;
    }

}
