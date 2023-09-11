package com.bankprototype.gateway.web.feign.fallback;

import com.bankprototype.gateway.web.dto.FinishRegistrationRequestDTO;
import com.bankprototype.gateway.web.feign.DealFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DealFeignClientFallbackFactory implements FallbackFactory<DealFeignClient> {
    @Override
    public DealFeignClient create(Throwable cause) {

        log.error("An exception occurred when calling the DealFeignClient", cause);

        return new DealFeignClient() {
            @Override
            public void completionRegistrationAndCalculateFullCredit(Long applicationId, FinishRegistrationRequestDTO requestDTO) {
                log.info("[Fallback.completionRegistrationAndCalculateFullCredit] >> applicationId: {}, requestDTO: {}", applicationId, requestDTO);

                log.error("An exception occurred when calling the completionRegistrationAndCalculateFullCredit method", cause);

            }

            @Override
            public void sendDocuments(Long applicationId) {
                log.info("[Fallback.sendDocuments] >> applicationId: {}", applicationId);

                log.error("An exception occurred when calling the sendDocuments method", cause);
            }

            @Override
            public void signDocuments(Long applicationId) {
                log.info("[Fallback.signDocuments] >> applicationId: {}", applicationId);

                log.error("An exception occurred when calling the signDocuments method", cause);
            }

            @Override
            public void codeDocuments(Long sesCode, Long applicationId) {
                log.info("[Fallback.codeDocuments] >> applicationId: {}", applicationId);

                log.error("An exception occurred when calling the codeDocuments method", cause);
            }

        };
    }
}
