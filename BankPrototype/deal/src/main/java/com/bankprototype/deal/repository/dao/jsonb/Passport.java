package com.bankprototype.deal.repository.dao.jsonb;


import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Passport{

    private String series;

    private String number;

    private String issueBranch;

    private LocalDate issueDate;
}
