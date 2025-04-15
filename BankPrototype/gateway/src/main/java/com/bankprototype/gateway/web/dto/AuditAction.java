package com.bankprototype.gateway.web.dto;

import com.bankprototype.gateway.web.dto.enumfordto.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AuditAction {
    @JsonIgnore
    private UUID id;
    @NonNull
    private Type type;
    @NonNull
    private Service service;
    @NonNull
    private String message;
}
