package com.bankprototype.gateway.web.feign;

import com.bankprototype.gateway.web.dto.FinishRegistrationRequestDTO;
import com.bankprototype.gateway.web.feign.fallback.DealFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(value = "deal", url = "http://localhost:8081/deal", fallbackFactory = DealFeignClientFallbackFactory.class)
public interface DealFeignClient {
    @PutMapping("/calculate/{applicationId}")
    void completionRegistrationAndCalculateFullCredit(@PathVariable(value = "applicationId") Long applicationId,
                                                      @Valid @RequestBody FinishRegistrationRequestDTO requestDTO);

    @PostMapping("/document/{applicationId}/send")
    void sendDocuments(@PathVariable(value = "applicationId") Long applicationId);

    @PostMapping("/document/{applicationId}/sign")
    void signDocuments(@PathVariable(value = "applicationId") Long applicationId);

    @PostMapping("/document/{applicationId}/code")
    void codeDocuments(@RequestParam(value = "sesCode") Long sesCode,
                       @PathVariable(value = "applicationId") Long applicationId);
}
