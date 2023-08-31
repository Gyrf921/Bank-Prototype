package com.bankprototype.dossier.service.impl;

import com.bankprototype.dossier.service.DossierConsumer;
import com.bankprototype.dossier.service.SendMailService;
import com.bankprototype.dossier.web.dto.EmailMassageDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DossierConsumerImpl implements DossierConsumer {

    private final SendMailService mailService;

    private final ObjectMapper objectMapper;


    @Override
    @KafkaListener(groupId = "dossier", topics = "finish_registration")
    public void consumeFinishRegistration(String message) {
        log.info("[consumeFinishRegistration] >> massage: {}", message);

        EmailMassageDTO email;

        try {
            email = objectMapper.readValue(message, EmailMassageDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        mailService.sendEmail(email, "Первичная регистрация заявки", "Заканчивая первичное оформление," +
                " ты делаешь первый шаг к своим целям и мечтам! Ты готов стать частью чего-то большего," +
                " и я уверен, что у тебя все получится. Вперед, преодолевай преграды и достигай вершин! Вместе с нашим кредитом!");

        log.info("[consumeFinishRegistration] << result void, email: {}", email);
    }


    @Override
    @KafkaListener(groupId = "dossier", topics = "create_documents")
    public void consumeCreateDocuments(String message) {
        log.info("[consumeCreateDocuments] >> message: {}", message);

        EmailMassageDTO email;
        try {
            email = objectMapper.readValue(message, EmailMassageDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        mailService.sendEmail(email, "Переход к оформлению документов", "Оформление документов - это следующий шаг на пути к твоим богатствам!" +
                " Ты создаешь основу для своего будущего успеха. Не забудь, что каждая отметка, каждая подпись в документе - это шаг вперед к твоей мечте." +
                " Иди вперед и не останавливайся!");

        log.info("[consumeCreateDocuments] << result void, email: {}", email);
    }

    @Override
    @KafkaListener(groupId = "dossier", topics = "send_documents")
    public void consumeSendDocuments(String message) {
        log.info("[consumeSendDocuments] >> message: {}", message);

        EmailMassageDTO email;
        try {
            email = objectMapper.readValue(message, EmailMassageDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        mailService.sendEmail(email, "Получение сформированных документов", "Поздравляю! Ты получил сформированные документы на почту." +
                " Это свидетельство твоего прогресса и упорства. Теперь реальность стала немного ближе к твоим мечтам. Не сомневайся в своих силах," +
                " иди вперед и достигни своих целей!");
        log.info("[consumeSendDocuments] << result void, email: {}", email);
    }

    @Override
    @KafkaListener(groupId = "dossier", topics = "credit_issued")
    public void consumeCreditIssued(String message) {
        log.info("[consumeCreditIssued] >> message: {}", message);

        EmailMassageDTO email;
        try {
            email = objectMapper.readValue(message, EmailMassageDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        mailService.sendEmail(email, "Ссылка на подписание документов", "Вот она - заветная ссылка на подписание документов!" +
                " Ты так близко к реализации своих мечт! Не бойся шагнуть в неизвестность и сделать этот решающий шаг. Уверен," +
                " что за каждой подписью скрывается новая возможность. Пусть каждый клик будет шагом к твоей победе!");

        log.info("[consumeCreditIssued] << result void, email: {}", email);
    }

    @Override
    @KafkaListener(groupId = "dossier", topics = "send_ses")
    public void consumeSendSes(String message) {
        log.info("[consumeSendSes] >> message: {}", message);

        EmailMassageDTO email;
        try {
            email = objectMapper.readValue(message, EmailMassageDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        mailService.sendEmail(email, "Получение SES кода", "Поздравляю с получением SES кода для подписания документов!" +
                " В этом коде заключены твоя сила и уверенность. Будь смелым и решительным при подписании." +
                " Это твой шанс отпечатать свой след на пути к успеху. Не сомневайся в своей способности преодоление любых препятствий!");

        log.info("[consumeSendSes] << result void, email: {}", email);
    }



    @Override
    @KafkaListener(groupId = "dossier", topics = "application_denied")
    public void consumeApplicationDenied(String message) {
        log.info("[consumeApplicationDenied] >> message: {}", message);

        EmailMassageDTO email;
        try {
            email = objectMapper.readValue(message, EmailMassageDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        mailService.sendEmail(email, "Отказ в выдаче кредита", "Принятие отказа по заявке на кредит -" +
                " это лишь страничка в твоей истории. Не забывай, что даже в неудаче есть уроки и возможности." +
                " Разочарование - это всего лишь новый вызов, новый толчок для развития. Встань после падения и" +
                " иди дальше, впереди ещё много возможностей стать лучше!");

        log.info("[consumeApplicationDenied] << result void, email: {}", email);
    }


}
