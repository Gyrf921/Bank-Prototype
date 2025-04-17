package org.bankprototype.audit.controller;


import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bankprototype.audit.controller.dto.AuditActionRequestDto;
import org.bankprototype.audit.controller.dto.AuditActionResponseDto;
import org.bankprototype.audit.service.AuditService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping( "/actions")
@Slf4j
public class AdminController {

    private final AuditService auditService;

    @GetMapping("/{id}")
    public ResponseEntity<AuditActionResponseDto> getAction(@PathVariable("id") String id) {
        return ResponseEntity.ok(auditService.getAction(UUID.fromString(id)));
    }

    @GetMapping()
    public ResponseEntity<List<AuditActionResponseDto>> getActions() {
        return ResponseEntity.ok(auditService.getActions());
    }

    @GetMapping("/page/{page}/size/{size}")
    public ResponseEntity<List<AuditActionResponseDto>> getAuditLogs(@PathVariable("page") Integer page, @PathVariable("size") Integer size, @RequestBody AuditActionRequestDto auditActionRequestDto) {
        return ResponseEntity
                .ok()
                .body(auditService.getActions(auditActionRequestDto, Pageable.ofSize(size).withPage(page)));
    }
}
