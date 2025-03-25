package com.bankprototype.gateway.web.feign.fallback;

import com.bankprototype.gateway.exception.ExceptionHandlerUtil;
import com.bankprototype.gateway.web.dto.AuditAction;
import com.bankprototype.gateway.web.dto.FinishRegistrationRequestDTO;
import com.bankprototype.gateway.web.feign.ApplicationFeignClient;
import com.bankprototype.gateway.web.feign.AuditFeignClient;
import com.bankprototype.gateway.web.feign.DealFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuditFeignClientFallbackFactory implements FallbackFactory<AuditFeignClient> {

    private final ExceptionHandlerUtil exceptionHandler;

    @Override
    public AuditFeignClient create(Throwable cause) {

        log.error("An exception occurred when calling the DealFeignClient", cause);

        return new AuditFeignClient() {
            @Override
            public ResponseEntity<AuditAction> getAction(String id) {
                log.info("[Fallback.getAction] >> id: {}", id);

                exceptionHandler.getExternalException(cause);

                log.info("[Fallback.getAction] << result: null");

                return ResponseEntity.internalServerError().body(null);
            }

            @Override
            public ResponseEntity<List<AuditAction>> getActions() {
                log.info("[Fallback.getActions]");

                exceptionHandler.getExternalException(cause);

                log.info("[Fallback.getActions] << result: null");

                return ResponseEntity.internalServerError().body(null);
            }

        };
    }
}
