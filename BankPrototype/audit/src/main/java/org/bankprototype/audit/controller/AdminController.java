package org.bankprototype.audit.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bankprototype.audit.domein.mapper.AuditActionMapper;
import org.bankprototype.audit.domein.model.AuditAction;
import org.bankprototype.audit.service.AuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping( "/api/v1/actions")
@Slf4j
public class AdminController {

    private final AuditService auditService;

    @GetMapping("/{id}")
    public ResponseEntity<AuditAction> getAction(@PathVariable("id") String id) {
        return ResponseEntity.ok(auditService.getAction(UUID.fromString(id)));
    }

    @GetMapping()
    public ResponseEntity<List<AuditAction>> getActions() {
        return ResponseEntity.ok(auditService.getActions());
    }
}
