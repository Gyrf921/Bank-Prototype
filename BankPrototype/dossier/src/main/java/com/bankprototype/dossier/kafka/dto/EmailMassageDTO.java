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
public class EmailMassageDTO {
    String address;
    Theme theme;
    Long applicationId;
}
