package com.bankprototype.gateway.web.controller;

import com.bankprototype.gateway.web.dto.AuditAction;
import com.bankprototype.gateway.web.feign.AuditFeignClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditFeignClient auditFeignClient;

    @Operation(summary = "Get audit action by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Action was found"),
            @ApiResponse(responseCode = "400", description = "Validation failed for some argument. Invalid input supplied"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @GetMapping("/{id}")
    public ResponseEntity<AuditAction> getAction(@PathVariable("id") String id) {
        log.info("[getAction] >> id: {}", id);
        return ResponseEntity.ok().body(auditFeignClient.getAction(id).getBody());
    }

    @Operation(summary = "Get all audit action")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Audit actions was found"),
            @ApiResponse(responseCode = "400", description = "Validation failed for some argument"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @GetMapping()
    public ResponseEntity<List<AuditAction>> getActions() {
        log.info("[getActions]");
        return ResponseEntity.ok().body(auditFeignClient.getActions().getBody());
    }

}
