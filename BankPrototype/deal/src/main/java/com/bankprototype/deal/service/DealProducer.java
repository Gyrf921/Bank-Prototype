package com.bankprototype.deal.service;


import com.bankprototype.deal.kafka.EmailMessageDTO;
import com.bankprototype.deal.kafka.enumfordto.Theme;

public interface DealProducer {

    EmailMessageDTO createMessage(Long applicationId, Theme theme);

    void sendMessage(EmailMessageDTO massageDTO, String topicName);

}
