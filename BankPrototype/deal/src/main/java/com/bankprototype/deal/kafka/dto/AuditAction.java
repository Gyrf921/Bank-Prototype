package com.bankprototype.deal.kafka.dto;

import com.bankprototype.deal.dao.Application;
import com.bankprototype.deal.kafka.dto.enumforkafka.Service;
import com.bankprototype.deal.kafka.dto.enumforkafka.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AuditAction {
    private UUID id;
    private Type type;
    private Service service;
    private String message;

    public static AuditAction create(Type type, Service service, Application application) {
        StringBuilder sb = new StringBuilder();
        sb.append(LocalDateTime.now()).append(" | ")
                .append("appId: ").append(application.getApplicationId()).append(" ; ")
                .append("status: ").append(application.getStatus()).append(" ; ")
                .append("clientId: ").append(application.getClientId().getClientId()).append(" ; ")
                .append("offer: ").append(application.getAppliedOffer()).append(" ; ");
        return AuditAction.builder()
                .type(type)
                .service(service)
                .message(sb.toString()).build();
    }
    public static AuditAction create(Type type, Service service, String message) {
        return AuditAction.builder()
                .type(type)
                .service(service)
                .message(message).build();
    }
}
