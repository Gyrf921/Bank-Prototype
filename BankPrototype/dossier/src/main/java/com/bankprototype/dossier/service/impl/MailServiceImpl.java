package com.bankprototype.dossier.service.impl;

import com.bankprototype.dossier.config.EmailTemplateConfig;
import com.bankprototype.dossier.exception.PrepareMimeMessageException;
import com.bankprototype.dossier.exception.SetModelToEmailTemplateException;
import com.bankprototype.dossier.exception.TemplateNotExistException;
import com.bankprototype.dossier.model.EmailContent;
import com.bankprototype.dossier.model.EmailInfo;
import com.bankprototype.dossier.service.MailService;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {

    private final EmailTemplateConfig emailTemplateConfig;

    private final JavaMailSender emailSender;


    @Value("${spring.mail.username}")
    private String emailFrom;

    @Override
    public void sendEmail(MimeMessage emailMessage) {
        log.info("[sendEmail] >> emailMessage: {}", emailMessage);

        emailSender.send(emailMessage);

        log.info("[sendEmail] << result void");
    }

    @Override
    public MimeMessage createEmailMimeMessage(EmailInfo emailInfo) {
        log.info("[createEmailMimeMessage] >> emailInfo: {}", emailInfo);

        MimeMessage mimeMessage = emailSender.createMimeMessage();

        prepareEmail(mimeMessage, emailInfo);

        log.info("[sendEmail] << result mimeMessage : {}", mimeMessage);

        return mimeMessage;
    }

    private void prepareEmail(MimeMessage mimeMessage, EmailInfo emailInfo) {
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setFrom(emailFrom);
            helper.setTo(emailInfo.getAddress());
            helper.setSubject(emailInfo.getEmailContent().getTheme());
            helper.setText(createEmailContent(emailInfo.getEmailContent()), true);
        } catch (MessagingException e) {
            log.error("[prepareEmail] MessagingException -> PrepareMimeMessageException: {}", e.getMessage());
            throw new PrepareMimeMessageException(e.getMessage());
        }
    }

    private String createEmailContent(EmailContent emailContent) {
        log.info("[createEmailContent] >> emailContent: {}", emailContent);
        try {
            String emailContentInHtml = FreeMarkerTemplateUtils
                    .processTemplateIntoString(emailTemplateConfig.templateEmail(), getDataMapForTemplate(emailContent));
            log.info("[createEmailContent] << emailContentInHtml: {}", emailContentInHtml);
            return emailContentInHtml;
        } catch (TemplateException e) {
            log.error("[createEmailContent] TemplateException -> SetModelToEmailTemplateException: {}", e.getMessage());
            throw new SetModelToEmailTemplateException(e.getMessage());
        } catch (IOException e) {
            log.error("[createEmailContent] IOException -> TemplateNotExistException: {}", e.getMessage());
            throw new TemplateNotExistException(e.getMessage());
        }
    }

    private Map<String, Object> getDataMapForTemplate(EmailContent emailContent) {
        Map<String, Object> model = new HashMap<>();
        model.put("applicationId", emailContent.getApplicationId());
        model.put("theme", emailContent.getTheme());
        model.put("text", emailContent.getText());
        log.info("[getDataMapForTemplate] >> result: {}", model);
        return model;
    }

}
