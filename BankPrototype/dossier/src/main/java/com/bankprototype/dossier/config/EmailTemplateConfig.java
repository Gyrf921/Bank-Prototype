package com.bankprototype.dossier.config;

import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class EmailTemplateConfig {

    private final freemarker.template.Configuration configurationEmail;

    @Bean
    public Template templateEmail() throws IOException {
        return configurationEmail.getTemplate("email.ftlh");
    }
}
