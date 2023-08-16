package com.bankprototype.deal;

import com.bankprototype.deal.web.feign.CreditConveyorFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//@EnableFeignClients(clients = CreditConveyorFeignClient.class)
public class DealApplication {
	public static void main(String[] args) {
		SpringApplication.run(DealApplication.class, args);
	}

}
