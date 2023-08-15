package com.bankprototype.deal.mapper;

import com.bankprototype.deal.repository.dao.Credit;
import com.bankprototype.deal.web.dto.CreditDTO;
import com.bankprototype.deal.web.dto.PaymentScheduleElement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-15T14:06:49+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
@Component
public class CreditMapperImpl implements CreditMapper {

    @Override
    public Credit creditDtoToCredit(CreditDTO creditDTO) {
        if ( creditDTO == null ) {
            return null;
        }

        Credit.CreditBuilder credit = Credit.builder();

        credit.insuranceEnable( creditDTO.getIsInsuranceEnabled() );
        credit.salaryClient( creditDTO.getIsSalaryClient() );
        credit.amount( creditDTO.getAmount() );
        credit.term( creditDTO.getTerm() );
        credit.monthlyPayment( creditDTO.getMonthlyPayment() );
        credit.rate( creditDTO.getRate() );
        credit.psk( creditDTO.getPsk() );
        List<PaymentScheduleElement> list = creditDTO.getPaymentSchedule();
        if ( list != null ) {
            credit.paymentSchedule( new ArrayList<PaymentScheduleElement>( list ) );
        }

        return credit.build();
    }
}
