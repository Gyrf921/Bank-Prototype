package com.bankprototype.deal.web.controller;

import com.bankprototype.deal.repository.dao.Application;
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

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/deal/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ApplicationService applicationService;

    @Operation(summary = "Get application by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application by id has been founded"),
            @ApiResponse(responseCode = "404", description = "Not found application with this id in database"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @GetMapping("/application/{applicationId}")
    public ResponseEntity<Application> getApplication(@PathVariable(value = "applicationId") Long applicationId) {
        log.info("[getApplication] >> applicationId: {}", applicationId);

        Application application = applicationService.getApplicationById(applicationId);

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

        List<Application> applications = applicationService.getAllApplication();

        log.info("[getAllApplications] << result: {}", applications);

        return ResponseEntity.ok().body(applications);
    }
}
