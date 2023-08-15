package com.bankprototype.deal.repository.dao.jsonb;

import com.bankprototype.deal.repository.dao.enumfordao.ApplicationStatus;
import com.bankprototype.deal.repository.dao.enumfordao.ChangeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StatusHistory {

    private ApplicationStatus status;

    private Timestamp time;

    private ChangeType changeType;

}
