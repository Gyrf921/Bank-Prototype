package com.bankprototype.deal.web.controller;

import com.bankprototype.deal.exception.SesCodeIsNotCorrectException;
import com.bankprototype.deal.kafka.EmailMessageDTO;
import com.bankprototype.deal.kafka.enumforkafka.Theme;
import com.bankprototype.deal.service.ApplicationService;
import com.bankprototype.deal.service.DealProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/deal/document")
@RequiredArgsConstructor
public class DocumentController {

    private final DealProducer dealProducer;

    private final ApplicationService applicationService;

    @Value("${topic-name.send-documents}")
    private String sendDocumentsTopicName;

    @Value("${topic-name.credit-issued}")
    private String creditIssuedTopicName;

    @Value("${topic-name.send-ses}")
    private String sendSesTopicName;

    @Operation(summary = "Request to send documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request to send documents has been completed"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping("/{applicationId}/send")
    public void sendDocuments(@PathVariable(value = "applicationId") Long applicationId) {
        log.info("[sendDocuments] >> applicationId:{}", applicationId);

        EmailMessageDTO massageDTO = dealProducer.createMessage(applicationId, Theme.SEND_DOCUMENTS);

        dealProducer.sendMessage(massageDTO, sendDocumentsTopicName);

        log.info("[sendDocuments] << result is void, message: {}, topic/theme: {}", massageDTO, Theme.SEND_DOCUMENTS.name());
    }

    @Operation(summary = "Request to sign documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request to sign documents has been completed"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PatchMapping("/{applicationId}/sign")
    public void signDocuments(@PathVariable(value = "applicationId") Long applicationId) {
        log.info("[signDocuments] >> applicationId:{}", applicationId);

        applicationService.updateSesCodeForApplication(applicationId);

        EmailMessageDTO massageDTO = dealProducer.createMessage(applicationId, Theme.SEND_SES);

        dealProducer.sendMessage(massageDTO, sendSesTopicName);

        log.info("[signDocuments] << result is void, message: {}, topic/theme: {}", massageDTO, Theme.SEND_SES.name());
    }

    @Operation(summary = "Signing of documents and set sign date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signing of documents has been completed"),
            @ApiResponse(responseCode = "400", description = "Mistake with ses code"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PatchMapping("/{applicationId}/code")
    public void codeDocuments(@RequestParam(value = "sesCode") Long sesCode,
                              @PathVariable(value = "applicationId") Long applicationId) {
        log.info("[codeDocuments] >> sesCode:{}, applicationId: {}", sesCode, applicationId);

        EmailMessageDTO massageDTO = null;

        if (applicationService.checkingCorrectnessSesCode(applicationId, sesCode)) {

            massageDTO = dealProducer.createMessage(applicationId, Theme.CREDIT_ISSUED);

            dealProducer.sendMessage(massageDTO, creditIssuedTopicName);
        } else {
            log.error("Ses code: \"{}\" is not correct", sesCode);
            throw new SesCodeIsNotCorrectException("Ses code: \"" + sesCode + "\" is not correct");
        }

        log.info("[codeDocuments] << result is void, message: {}, topic/theme: {}", massageDTO, Theme.CREDIT_ISSUED.name());

    }

}
