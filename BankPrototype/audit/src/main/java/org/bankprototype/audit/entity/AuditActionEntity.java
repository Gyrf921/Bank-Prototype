package org.bankprototype.audit.entity;


import lombok.*;
import org.bankprototype.audit.entity.enums.Service;
import org.bankprototype.audit.entity.enums.Type;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "Audit_Action")
public class AuditActionEntity {
    @Id
    private UUID id;
    @Indexed
    private Type type;
    @Indexed
    private Service service;
    private String message;
}
