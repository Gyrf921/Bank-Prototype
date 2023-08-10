package com.bankprototype.deal.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreditDTO {
    private BigDecimal amount; //Запрашиваемая сумма
    private Integer term; //срок
    private BigDecimal monthlyPayment;
    private BigDecimal rate; //ставка
    private BigDecimal psk; // полная стоимость кредита
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
    private List<PaymentScheduleElement> paymentSchedule; //график платежей

}
