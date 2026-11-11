package com.example.library.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.runtime.KieContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroolsConfig {
    @Bean
    public KieContainer kieContainer() {
        KieServices services = KieServices.Factory.get();
        KieFileSystem files = services.newKieFileSystem();
        files.write(services.getResources()
                .newClassPathResource("rules/borrowing-rules.drl")
                .setSourcePath("src/main/resources/rules/borrowing-rules.drl"));
        KieBuilder builder = services.newKieBuilder(files).buildAll();
        if (builder.getResults().hasMessages(Message.Level.ERROR)) {
            throw new IllegalStateException("Drools rule errors: " + builder.getResults());
        }
        return services.newKieContainer(services.getRepository().getDefaultReleaseId());
    }
}
