package com.bankprototype.deal.dao.jsonb;

import com.bankprototype.deal.dao.enumfordao.ApplicationStatus;
import com.bankprototype.deal.dao.enumfordao.ChangeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StatusHistory {

    private ApplicationStatus status;

    private LocalDateTime time;

    private ChangeType changeType;

}
