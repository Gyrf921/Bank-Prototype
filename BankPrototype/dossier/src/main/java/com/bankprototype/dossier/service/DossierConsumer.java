package com.bankprototype.dossier.service;

import com.bankprototype.dossier.web.dto.EmailMassageDTO;


public interface DossierConsumer {

    void consumeFinishRegistration(String massageDTO);
    void consumeCreateDocuments(String massageDTO);
    void consumeSendDocuments(String massageDTO);
    void consumeSendSes(String massageDTO);
    void consumeCreditIssued(String massageDTO);
    void consumeApplicationDenied(String massageDTO);
}
