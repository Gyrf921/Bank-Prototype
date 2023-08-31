package com.bankprototype.deal.service;


import com.bankprototype.deal.web.dto.EmailMassageDTO;

public interface DealProducer {

     void sendMessage(EmailMassageDTO massageDTO, String topicName);

}
