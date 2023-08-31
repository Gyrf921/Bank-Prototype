package com.bankprototype.deal.web.dto;

import com.bankprototype.deal.web.dto.enumfordto.Theme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmailMassageDTO {
    String address;
    Theme theme;
    Long applicationId;
}
