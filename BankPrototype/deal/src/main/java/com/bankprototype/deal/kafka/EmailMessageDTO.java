package com.bankprototype.deal.kafka;

import com.bankprototype.deal.kafka.enumforkafka.Theme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmailMessageDTO {
    String address;
    Theme theme;
    Long applicationId;
    Long sesCode;
}
