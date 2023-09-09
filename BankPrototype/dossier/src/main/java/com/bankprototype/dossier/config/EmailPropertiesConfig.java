package com.bankprototype.dossier.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mail")
@Data
public class EmailPropertiesConfig {

    private String finishRegistrationTheme;

    private String finishRegistrationText;

    private String createDocumentsTheme;

    private String createDocumentsText;

    private String sendDocumentsTheme;

    private String sendDocumentsText;

    private String creditIssuedTheme;

    private String creditIssuedText;

    private String sendSesTheme;

    private String sendSesText;

    private String applicationDeniedTheme;

    private String applicationDeniedText;


}
