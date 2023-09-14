package com.bankprototype.gateway.web.controller;

import com.bankprototype.gateway.web.dto.FinishRegistrationRequestDTO;
import com.bankprototype.gateway.web.dto.LoanApplicationRequestDTO;
import com.bankprototype.gateway.web.dto.LoanOfferDTO;
import com.bankprototype.gateway.web.feign.ApplicationFeignClient;
import com.bankprototype.gateway.web.feign.DealFeignClient;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationFeignClient applicationFeignClient;

    private final DealFeignClient dealFeignClient;

    @Operation(summary = "Create loan application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client and application was created. Loan Offers have been calculated"),
            @ApiResponse(responseCode = "400", description = "Validation failed for some argument. Invalid input supplied"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping
    public ResponseEntity<List<LoanOfferDTO>> calculatePossibleLoanOffers(@Valid @RequestBody LoanApplicationRequestDTO requestDTO) {
        log.info("[calculatePossibleLoanOffers] >> requestDTO: {}", requestDTO);

        List<LoanOfferDTO> listLoanOffers = applicationFeignClient.calculatePossibleLoanOffers(requestDTO).getBody();

        log.info("[calculatePossibleLoanOffers] << result: {}", listLoanOffers);

        return ResponseEntity.ok().body(listLoanOffers);
    }

    @Operation(summary = "Chose 1 of 4 offers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan Offer has been choose"),
            @ApiResponse(responseCode = "400", description = "Validation failed for some argument"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PostMapping("/apply")
    public void chooseOneOfTheOffers(@Valid @RequestBody LoanOfferDTO loanOfferDTO) {
        log.info("[chooseOneOfTheOffers] >> loanOfferDTO: {}", loanOfferDTO);

        applicationFeignClient.chooseOneOfTheOffers(loanOfferDTO);

        log.info("[chooseOneOfTheOffers] << result is void, Loan Offer have been choose.");
    }


    @Operation(summary = "Finish registration", description = "Completion of registration and calculation of full loan terms")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credit has been created. Registration has been finished"),
            @ApiResponse(responseCode = "400", description = "Validation failed for some argument. Invalid input supplied"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @PutMapping("/registration/{applicationId}")
    public void completionRegistrationAndCalculateFullCredit(@PathVariable(value = "applicationId") Long applicationId,
                                                             @Valid @RequestBody FinishRegistrationRequestDTO requestDTO) {
        log.info("[completionRegistrationAndCalculateFullCredit] >> applicationId:{}, requestDTO: {}", applicationId, requestDTO);

        dealFeignClient.completionRegistrationAndCalculateFullCredit(applicationId, requestDTO);

        log.info("[completionRegistrationAndCalculateFullCredit] << result is void");
    }
}
