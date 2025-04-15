package com.bankprototype.gateway.web.feign;

import com.bankprototype.gateway.web.dto.AuditAction;
import com.bankprototype.gateway.web.feign.fallback.ApplicationFeignClientFallbackFactory;
import com.bankprototype.gateway.web.feign.fallback.AuditFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(value = "audit", url = "audit:8086/api/v1/actions", fallbackFactory = AuditFeignClientFallbackFactory.class)
public interface AuditFeignClient {
    @GetMapping("/{id}")
    ResponseEntity<AuditAction> getAction(@PathVariable("id") String id);
    @GetMapping()
    ResponseEntity<List<AuditAction>> getActions();
}
