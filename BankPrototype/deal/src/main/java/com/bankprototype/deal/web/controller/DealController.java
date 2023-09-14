package com.bankprototype.deal.web.controller;

import com.bankprototype.deal.exception.ResourceNotFoundException;
import com.bankprototype.deal.service.impl.DealService;
import com.bankprototype.deal.web.dto.FinishRegistrationRequestDTO;
import com.bankprototype.deal.web.dto.LoanApplicationRequestDTO;
import com.bankprototype.deal.web.dto.LoanOfferDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealController {


    private final DealService dealService;

    @Operation(summary = "Calculate 4 loan offers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client and application was created. Loan Offers have been calculated"),
            @ApiResponse(responseCode = "400", description = "Validation failed for some argument. Invalid input supplied"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping("/application")
    public ResponseEntity<List<LoanOfferDTO>> calculatePossibleLoanOffers(@Valid @RequestBody LoanApplicationRequestDTO requestDTO) {
        log.info("[calculatePossibleLoanOffers] >> requestDTO: {}", requestDTO);

        List<LoanOfferDTO> listLoanOffers = dealService.createLoanApplication(requestDTO);

        log.info("[calculatePossibleLoanOffers] << result: {}", listLoanOffers);

        return ResponseEntity.ok().body(listLoanOffers);
    }

    @Operation(summary = "Choose one of the loan offers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status history for application has been updated"),
            @ApiResponse(responseCode = "404", description = "Not found application", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = ResourceNotFoundException.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PutMapping("/offer")
    public void chooseOneOfTheOffers(@RequestBody LoanOfferDTO loanOfferDTO) {
        log.info("[chooseOneOfTheOffers] >> loanOfferDTO: {}", loanOfferDTO);

        dealService.chooseOneOfTheOffers(loanOfferDTO);

        log.info("[chooseOneOfTheOffers] << result: void");
    }


    @Operation(summary = "Completion registration", description = "Completion of registration and calculation of full loan terms")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credit has been created. Registration has been finished"),
            @ApiResponse(responseCode = "400", description = "Validation failed for some argument. Invalid input supplied"),
            @ApiResponse(responseCode = "404", description = "Not found some resource in database"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PutMapping("/calculate/{applicationId}")
    public void completionRegistrationAndCalculateFullCredit(@PathVariable(value = "applicationId") Long applicationId,
                                                             @Valid @RequestBody FinishRegistrationRequestDTO requestDTO) {
        log.info("[completionRegistrationAndCalculateFullCredit] >> applicationId:{}, requestDTO: {}", applicationId, requestDTO);

        dealService.finishRegistration(applicationId, requestDTO);

        log.info("[completionRegistrationAndCalculateFullCredit] << result: void");
    }


}
