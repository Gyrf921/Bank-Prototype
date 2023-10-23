package com.bankprototype.deal.web.dto;

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
public class ApplicationStatusHistoryDTO {

    private ApplicationStatus status;

    private LocalDateTime time;

    private ChangeType changeType;

}
