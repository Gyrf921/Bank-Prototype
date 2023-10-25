package com.bankprototype.dossier.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailContent {

    private Long applicationId;

    private String theme;

    private String text;

}
