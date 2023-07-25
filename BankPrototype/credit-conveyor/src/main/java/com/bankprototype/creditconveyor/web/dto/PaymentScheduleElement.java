package com.bankprototype.creditconveyor.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentScheduleElement
{
    private Integer number;
    private LocalDate date;
    private BigDecimal totalPayment; //общая сумма платежа;
    private BigDecimal interestPayment; //выплата процентов;
    private BigDecimal debtPayment; //выплата долга;
    private BigDecimal remainingDebt; //оставшийся долг;
}
