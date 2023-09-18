package com.bankprototype.gateway.web.controller;

import com.bankprototype.gateway.web.feign.DealFeignClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/document")
@RequiredArgsConstructor
public class DocumentController {

    private final DealFeignClient dealFeignClient;

    @Operation(summary = "Create document request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request to send documents has been completed"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping("/{applicationId}")
    public void sendDocuments(@PathVariable(value = "applicationId") Long applicationId) {
        log.info("[sendDocuments] >> applicationId:{}", applicationId);

        dealFeignClient.sendDocuments(applicationId);

        log.info("[sendDocuments] << result is void");
    }

    @Operation(summary = "Request to sign documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request to sign documents has been completed"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping("/{applicationId}/sign")
    public void signDocuments(@PathVariable(value = "applicationId") Long applicationId) {
        log.info("[signDocuments] >> applicationId:{}", applicationId);

        dealFeignClient.signDocuments(applicationId);

        log.info("[signDocuments] << result is void");
    }

    @Operation(summary = "Signing of documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signing of documents has been completed"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping("/{applicationId}/code")
    public void codeDocuments(@RequestParam(value = "sesCode") Long sesCode,
                              @PathVariable(value = "applicationId") Long applicationId) {
        log.info("[codeDocuments] >> sesCode:{}, applicationId: {}", sesCode, applicationId);

        dealFeignClient.codeDocuments(sesCode, applicationId);

        log.info("[codeDocuments] << result is void");

    }
}
