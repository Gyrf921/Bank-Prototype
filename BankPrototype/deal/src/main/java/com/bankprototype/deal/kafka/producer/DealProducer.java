package com.bankprototype.deal.kafka.producer;


import com.bankprototype.deal.kafka.dto.EmailMessageDTO;
import com.bankprototype.deal.kafka.dto.enumforkafka.Theme;

public interface DealProducer {

    EmailMessageDTO createMessage(Long applicationId, Theme theme);

    void sendMessage(EmailMessageDTO massageDTO, String topicName);

}
