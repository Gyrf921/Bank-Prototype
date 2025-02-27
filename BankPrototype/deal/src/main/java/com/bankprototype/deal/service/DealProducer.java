package com.bankprototype.deal.service;


import com.bankprototype.deal.web.kafka.EmailMessageDTO;
import com.bankprototype.deal.web.kafka.enumforkafka.Theme;

public interface DealProducer {

    EmailMessageDTO createMessage(Long applicationId, Theme theme);

    void sendMessage(EmailMessageDTO massageDTO, String topicName);

}
