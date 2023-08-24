package com.bankprototype.deal.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PaymentScheduleElement {
    private Integer number;
    private Timestamp date;
    private BigDecimal totalPayment; //общая сумма платежа;
    private BigDecimal interestPayment; //выплата процентов;
    private BigDecimal debtPayment; //выплата долга;
    private BigDecimal remainingDebt; //оставшийся долг;
}
