package com.bankprototype.gateway;

import com.bankprototype.gateway.web.feign.ApplicationFeignClient;
import com.bankprototype.gateway.web.feign.DealFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(clients = {ApplicationFeignClient.class, DealFeignClient.class})
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
