package org.bankprototype.audit.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bankprototype.audit.entity.enums.Service;
import org.bankprototype.audit.entity.enums.Type;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AuditActionResponseDto {
    @JsonIgnore
    private UUID id;
    private Type type;
    private Service service;
    private String message;
}
