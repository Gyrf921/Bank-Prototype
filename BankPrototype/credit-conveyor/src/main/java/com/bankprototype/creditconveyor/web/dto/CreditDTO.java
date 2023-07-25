package com.bankprototype.creditconveyor.web.dto;

import java.math.BigDecimal;
import java.util.List;

public class CreditDTO {
    private BigDecimal amount;
    private Integer term; //срок
    private BigDecimal monthlyPayment;
    private BigDecimal rate; //ставка
    private BigDecimal psk; // полная стоимость кредита
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
    private List<PaymentScheduleElement> paymentSchedule; //график платежей
}
