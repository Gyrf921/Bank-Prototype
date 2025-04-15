package org.bankprototype.audit.domein.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bankprototype.audit.domein.enums.*;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AuditAction {
    @JsonIgnore
    private UUID id;
    private Type type;
    private Service service;
    private String message;
}
