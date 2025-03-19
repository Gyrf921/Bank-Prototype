package org.bankprototype.audit.domein.model;

import org.bankprototype.audit.domein.enums.*;

import java.util.UUID;

public class AuditAction {
    private UUID id;
    private Type type;
    private Service service;
    private String message;
}
