package com.bankprototype.dossier.service;

import com.bankprototype.dossier.kafka.dto.EmailMassageDTO;

public interface SendMailService {
    void sendEmail(EmailMassageDTO messageText, String theme, String text);
}
