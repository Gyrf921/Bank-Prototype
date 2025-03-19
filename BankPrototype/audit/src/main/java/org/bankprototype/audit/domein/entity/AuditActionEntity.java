package org.bankprototype.audit.domein.entity;


import lombok.*;
import org.bankprototype.audit.domein.enums.Service;
import org.bankprototype.audit.domein.enums.Type;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash
public class AuditActionEntity {
    @Id
    private UUID id;
    private Type type;
    private Service service;
    private String message;
}
