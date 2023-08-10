package com.bankprototype.deal.repository.dao.jsonb;


import lombok.*;

import java.sql.Timestamp;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Passport{

    private String series;

    private String number;

    private String issueBranch;

    private Timestamp issueDate;
}
