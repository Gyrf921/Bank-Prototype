package com.bankprototype.dossier.service;

import com.bankprototype.dossier.web.dto.EmailMassageDTO;

public interface SendMailService {
    void sendEmail(EmailMassageDTO messageText, String theme, String text);
}
