package com.bankprototype.dossier.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "mail")
@Data
public class EmailPropertiesConfig {

    String finishRegistrationTheme;

    String finishRegistrationText;

    String createDocumentsTheme;

    String createDocumentsText;

    String sendDocumentsTheme;

    String sendDocumentsText;

    String creditIssuedTheme;

    String creditIssuedText;

    String sendSesTheme;

    String sendSesText;

    String applicationDeniedTheme;

    String applicationDeniedText;


}
