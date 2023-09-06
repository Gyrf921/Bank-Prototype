package com.bankprototype.deal.web.controller;

import com.bankprototype.deal.kafka.EmailMessageDTO;
import com.bankprototype.deal.kafka.enumfordto.Theme;
import com.bankprototype.deal.service.DealProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/document")
@RequiredArgsConstructor
public class DocumentController {

    private final DealProducer dealProducer;

    @Value("${topic-name.send-documents}")
    private String sendDocumentsTopicName;

    @Value("${topic-name.credit-issued}")
    private String creditIssuedTopicName;

    @Value("${topic-name.send-ses}")
    private String sendSesTopicName;


    @Operation(summary = "Request to send documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Possible Loan Offers have been calculated"),
            @ApiResponse(responseCode = "400", description = "Validation failed for some argument"),
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
            @ApiResponse(responseCode = "200", description = "Loan Offer have been choose"),
            @ApiResponse(responseCode = "400", description = "Validation failed for some argument"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping("/{applicationId}/sign")
    public void signDocuments(@PathVariable(value = "applicationId") Long applicationId) {
        log.info("[signDocuments] >> applicationId:{}", applicationId);

        EmailMessageDTO massageDTO = dealProducer.createMessage(applicationId, Theme.CREDIT_ISSUED);

        dealProducer.sendMessage(massageDTO, creditIssuedTopicName);

        log.info("[signDocuments] << result is void, message: {}, topic/theme: {}", massageDTO, Theme.CREDIT_ISSUED.name());
    }

    @Operation(summary = "Signing of documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan Offer have been choose"),
            @ApiResponse(responseCode = "400", description = "Validation failed for some argument"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping("/{applicationId}/code")
    public void codeDocuments(@PathVariable(value = "applicationId") Long applicationId) {
        log.info("[codeDocuments] >> applicationId:{}", applicationId);

        EmailMessageDTO massageDTO = dealProducer.createMessage(applicationId, Theme.SEND_SES);

        dealProducer.sendMessage(massageDTO, sendSesTopicName);

        log.info("[codeDocuments] << result is void, message: {}, topic/theme: {}", massageDTO, Theme.SEND_SES.name());

    }

}
