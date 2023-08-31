package com.bankprototype.dossier.service.impl;

import com.bankprototype.dossier.service.SendMailService;
import com.bankprototype.dossier.web.dto.EmailMassageDTO;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final Configuration configuration;

    @Override
    public void sendEmail(EmailMassageDTO messageDTO, String theme, String text) {
        log.info("[sendEmail] >> messageDTO: {}, theme: {}, text: {}...", messageDTO, theme, text.substring(0, 30));

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            helper.setFrom("gyrf921gyrf921@yandex.ru");
            helper.setTo(messageDTO.getAddress());
            helper.setSubject(theme);
            String emailContent = getEmailContent(messageDTO, theme, text);
            helper.setText(emailContent, true);

        } catch (MessagingException | TemplateException | IOException e) {
            //Todo стоит ли всё это обернуть в своё собственнои исключение?
            throw new RuntimeException(e);
        }

        emailSender.send(mimeMessage);

        log.info("[sendEmail] << result void, email: {}", mimeMessage);
    }

    private String getEmailContent(EmailMassageDTO emailMassageDTO, String theme, String textForUser) throws IOException, TemplateException {
        log.info("[sendEmail] >> emailMassageDTO: {}, theme: {}, textForUser: {}...", emailMassageDTO, theme, textForUser.substring(0, 30));

        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();

        model.put("emailMassageDTO", emailMassageDTO);
        model.put("theme", theme);
        model.put("textForUser", textForUser);


        configuration.getTemplate("email.ftlh").process(model, stringWriter);


        String emailContent = stringWriter.getBuffer().toString();

        log.info("[sendEmail] << result: {}", emailContent);

        return emailContent;
    }

}
