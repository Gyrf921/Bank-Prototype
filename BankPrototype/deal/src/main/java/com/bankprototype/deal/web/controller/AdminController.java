package com.bankprototype.deal.web.controller;

import com.bankprototype.deal.dao.Application;
import com.bankprototype.deal.kafka.dto.AuditAction;
import com.bankprototype.deal.kafka.dto.enumforkafka.Service;
import com.bankprototype.deal.kafka.dto.enumforkafka.Type;
import com.bankprototype.deal.kafka.producer.AuditProducer;
import com.bankprototype.deal.service.ApplicationService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/deal/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ApplicationService applicationService;

    private final AuditProducer audit;

    @Operation(summary = "Get application by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application by id has been founded"),
            @ApiResponse(responseCode = "404", description = "Not found application with this id in database"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @GetMapping("/application/{applicationId}")
    public ResponseEntity<Application> getApplication(@PathVariable(value = "applicationId") Long applicationId) {
        log.info("[getApplication] >> applicationId: {}", applicationId);

        audit.send(AuditAction.create(Type.START, Service.APPLICATION, LocalDateTime.now() + " | appId" + applicationId));
        Application application = applicationService.getApplicationById(applicationId);
        audit.send(AuditAction.create(Type.SUCCESS, Service.APPLICATION, LocalDateTime.now() + " | application" + application));

        log.info("[getApplication] << result: {}", application);

        return ResponseEntity.ok().body(application);
    }

    @Operation(summary = "Get all application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application by id has been founded"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @GetMapping("/application")
    public ResponseEntity<List<Application>> getAllApplications() {
        log.info("[getAllApplications] >> without parameters");

        audit.send(AuditAction.create(Type.START, Service.APPLICATION, LocalDateTime.now() + " - try to take all applications"));
        List<Application> applications = applicationService.getAllApplication();
        audit.send(AuditAction.create(Type.SUCCESS, Service.APPLICATION, LocalDateTime.now() + " | applications.count" + applications.size()));

        log.info("[getAllApplications] << result: {}", applications);

        return ResponseEntity.ok().body(applications);
    }
}
