package com.bankprototype.dossier.kafka.dto;

import com.bankprototype.dossier.kafka.dto.enamfordto.Theme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessageDTO {
    private String address;

    private Theme theme;

    private Long applicationId;

    private Long sesCode;
}
