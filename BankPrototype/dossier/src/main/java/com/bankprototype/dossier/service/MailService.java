package com.bankprototype.dossier.service;

import com.bankprototype.dossier.model.EmailInfo;

import javax.mail.internet.MimeMessage;

public interface MailService {
    void sendEmail(MimeMessage emailMessage);

    MimeMessage createEmailMimeMessage(EmailInfo emailInfo);
}
